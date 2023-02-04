package com.likelion.devroutine.participant.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.invite.dto.InviteCreateResponse;
import com.likelion.devroutine.invite.repository.InviteRepository;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.dto.ParticipationChallengeDto;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.enumerate.ResponseMessage;
import com.likelion.devroutine.participant.exception.DuplicatedParticipationException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.participant.exception.ParticipationNotFoundException;
import com.likelion.devroutine.participant.exception.RejectCancelException;
import com.likelion.devroutine.participant.repository.ParticipationRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.dto.UserResponse;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final InviteRepository inviteRepository;
    private final FollowRepository followRepository;

    public ParticipationService(ParticipationRepository participationRepository, UserRepository userRepository, ChallengeRepository challengeRepository, InviteRepository inviteRepository, FollowRepository followRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.inviteRepository = inviteRepository;
        this.followRepository = followRepository;
    }

    @Transactional
    public ParticipationResponse cancelChallenge(String oauthId, Long challengeId) {
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        if(challenge.getUserId().equals(user.getId())){
            throw new RejectCancelException();
        }
        Participation participation =getParticipant(user, challenge);
        isProgressChallenge(challenge.getStartDate());
        participationRepository.delete(participation);
        return ParticipationResponse.builder()
                .challengeId(challenge.getId())
                .message(ResponseMessage.CHALLENGE_CANCEL_SUCCESS.getMessage())
                .build();
    }
    @Transactional
    public InviteCreateResponse inviteUser(String inviterOauthId, Long challengeId, Long inviteeId) {
        User inviter=getUser(inviterOauthId);
        User invitee=userRepository.findById(inviteeId)
                .orElseThrow(()->new UserNotFoundException());
        validateFollower(inviter, invitee);
        Challenge challenge=getChallenge(challengeId);
        matchWriterAndUser(inviter, challenge);
        isProgressChallenge(challenge.getStartDate());
        Invite savedInvite=inviteRepository.save(Invite.createInvite(challenge.getId(), inviter.getId(), invitee.getId()));
        return InviteCreateResponse.builder()
                .inviterId(savedInvite.getInviterId())
                .challengeId(savedInvite.getChallengeId())
                .inviteeId(savedInvite.getInviteeId())
                .message(com.likelion.devroutine.invite.enumerate.ResponseMessage.INVITE_SUCCESS.getMessage())
                .build();
    }
    public ParticipationChallengeDto findByParticipateChallenge(String oauthId, Long challengeId){
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        Participation participation =getParticipant(user, challenge);
        List<Participation> participations = participationRepository.findAllByChallenge(challenge);
        return ParticipationChallengeDto.toResponse(participation, ChallengeHashTagResponse.of(challenge.getChallengeHashTags()), UserResponse.toList(getChallengeParticipants(participations)));
    }

    public List<User> getChallengeParticipants(List<Participation> participations){
        return participations.stream().map(participant -> participant.getUser())
                .collect(Collectors.toList());
    }
    public Participation getParticipant(User user, Challenge challenge){
        return participationRepository.findByUserAndChallenge(user, challenge)
                .orElseThrow(()-> new ParticipationNotFoundException());
    }

    public User getUser(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());
        return user;
    }

    public Challenge getChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException());
        return challenge;
    }

    public boolean isViewable(Challenge challenge, User user){
        if(challenge.getVigibility() || !inviteRepository.findAllByChallengeIdAndInviteeId(challenge.getId(), user.getId()).isEmpty()){
            return true;
        }
        throw new InaccessibleChallengeException();
    }

    public boolean isProgressChallenge(LocalDate startDate) {
        if (LocalDate.now().isAfter(startDate))
            throw new InProgressingChallengeException();
        return false;
    }

    //invitee가 inviter를 팔로우하고 있는지 확인하는 함수
    private void validateFollower(User inviter, User invitee) {
        Follow follow=followRepository.findByFollowerIdAndFollowingId(inviter.getId(), invitee.getId())
                .orElseThrow(()->new FollowingNotFoundException());
    }

    private boolean matchWriterAndUser(User user, Challenge challenge) {
        if(!user.getId().equals(challenge.getUserId())){
            throw new InvalidPermissionException();
        }
        return true;
    }
}
