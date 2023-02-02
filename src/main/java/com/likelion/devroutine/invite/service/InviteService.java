package com.likelion.devroutine.invite.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.invite.dto.*;
import com.likelion.devroutine.invite.enumerate.ResponseMessage;
import com.likelion.devroutine.invite.exception.BadRequestInviteAcceptException;
import com.likelion.devroutine.invite.exception.InviteNotFoundException;
import com.likelion.devroutine.invite.repository.InviteRepository;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.exception.DuplicatedParticipationException;
import com.likelion.devroutine.participant.repository.ParticipationRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class InviteService {
    private final InviteRepository inviteRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final FollowRepository followRepository;
    private final ParticipationRepository participationRepository;

    public InviteService(InviteRepository inviteRepository, UserRepository userRepository, ChallengeRepository challengeRepository, FollowRepository followRepository, ParticipationRepository participationRepository) {
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.followRepository = followRepository;
        this.participationRepository = participationRepository;
    }

    public List<FollowerResponse> findFollowers(String oauthId, Long challengeId) {
        User user = getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        matchWriterAndUser(user, challenge);
        List<Follow> followers=followRepository.findByFollowerId(user.getId());
        return FollowerResponse.of(followers);
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
                .message(ResponseMessage.INVITE_SUCCESS.getMessage())
                .build();
    }

    @Transactional
    public InviteResponse acceptInvite(String oauthId, Long inviteId) {
        User user=getUser(oauthId);
        Invite invite=getInvite(inviteId);
        validateInvitee(invite, user);
        Challenge challenge=getChallenge(invite.getChallengeId());
        validateParticipate(user, challenge);
        Participation savedParticipation=participationRepository.save(Participation.createParticipant(user, challenge));
        return InviteResponse.builder()
                .challengeId(savedParticipation.getChallenge().getId())
                .message(ResponseMessage.INVITE_ACCEPT.getMessage())
                .build();
    }

    public List<InviterResponse> findInviters(String oauthId) {
        User user=getUser(oauthId);
        List<Challenge> invites=inviteRepository.findInviterByInviteeId(user.getId());

        return InviterResponse.toList(invites);
    }

    public List<InviteeResponse> findInvitees(String oauthId) {
        User user=getUser(oauthId);
        List<InviteeResponse> inviteeResponses=inviteRepository.findInviteeByInviterId(user.getId());

        return inviteeResponses;
    }
    @Transactional
    public InviteResponse rejectInvite(String oauthId, Long challengeId) {
        User user=getUser(oauthId);
        List<Invite> invites=inviteRepository.findAllByChallengeIdAndInviteeId(challengeId, user.getId());
        if(invites.isEmpty()){
            throw new InviteNotFoundException();
        }
        invites.forEach(invite->invite.deleteInvite());
        return InviteResponse.builder()
                .challengeId(challengeId)
                .message(ResponseMessage.INVITE_REJECT.getMessage())
                .build();
    }

    @Transactional
    public InviteCancelResponse cancelInvite(String oauthId, Long inviteId) {
        User user=getUser(oauthId);
        Invite invite=getInvite(inviteId);
        invite.deleteInvite();
        return InviteCancelResponse.builder()
                .message(ResponseMessage.INVITE_CANCEL.getMessage())
                .build();
    }

    private boolean validateInvitee(Invite invite, User user) {
        if(invite.getInviteeId().equals(user.getId())){
            return true;
        }
        throw new BadRequestInviteAcceptException();
    }

    //invitee가 inviter를 팔로우하고 있는지 확인하는 함수
    private void validateFollower(User inviter, User invitee) {
        Follow follow=followRepository.findByFollowerIdAndFollowingId(inviter.getId(), invitee.getId())
                .orElseThrow(()->new FollowingNotFoundException());
    }

    private boolean matchWriterAndUser(User user, Challenge challenge) {
        if(!user.getId().equals(challenge.getUser().getId())){
            throw new InvalidPermissionException();
        }
        return true;
    }

    private Challenge getChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(()->new ChallengeNotFoundException());
    }

    public User getUser(String oauthId){
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(()->new UserNotFoundException());
    }

    public Invite getInvite(Long inviteId){
        return inviteRepository.findById(inviteId)
                .orElseThrow(()-> new InviteNotFoundException());
    }
    public boolean isProgressChallenge(LocalDate startDate) {
        if (LocalDate.now().isAfter(startDate))
            throw new InProgressingChallengeException();
        return false;
    }
    public boolean validateParticipate(User user, Challenge challenge){
        isProgressChallenge(challenge.getStartDate());
        validateDuplicateParticipate(user, challenge);
        return true;
    }
    public boolean validateDuplicateParticipate(User user, Challenge challenge){
        participationRepository.findByUserAndChallenge(user, challenge)
                .ifPresent(participant->{
                    throw new DuplicatedParticipationException();
                });
        return true;
    }
}
