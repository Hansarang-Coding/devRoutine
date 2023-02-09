package com.likelion.devroutine.participant.service;

import com.likelion.devroutine.alarm.domain.Alarm;
import com.likelion.devroutine.alarm.enumurate.AlarmType;
import com.likelion.devroutine.alarm.repository.AlarmRepository;
import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.dto.CertificationResponse;
import com.likelion.devroutine.certification.repository.CertificationRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.ChallengeDto;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import com.likelion.devroutine.hashtag.repository.ChallengeHashTagRepository;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.invite.dto.InviteCreateResponse;
import com.likelion.devroutine.invite.repository.InviteRepository;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.dto.ParticipationChallengeDto;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.dto.ParticipationSortResponse;
import com.likelion.devroutine.participant.enumerate.ResponseMessage;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.participant.exception.ParticipationNotFoundException;
import com.likelion.devroutine.participant.exception.RejectCancelException;
import com.likelion.devroutine.participant.repository.ParticipationRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.dto.UserResponse;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final InviteRepository inviteRepository;
    private final FollowRepository followRepository;
    private final ChallengeHashTagRepository challengeHashTagRepository;
    private final CertificationRepository certificationRepository;

    private final AlarmRepository alarmRepository;

    public ParticipationService(ParticipationRepository participationRepository, UserRepository userRepository, ChallengeRepository challengeRepository
            , InviteRepository inviteRepository, FollowRepository followRepository, ChallengeHashTagRepository challengeHashTagRepository, CertificationRepository certificationRepository, AlarmRepository alarmRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.inviteRepository = inviteRepository;
        this.followRepository = followRepository;
        this.challengeHashTagRepository = challengeHashTagRepository;
        this.certificationRepository = certificationRepository;
        this.alarmRepository = alarmRepository;
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
        saveInviteAlarm(invitee,inviter.getId());
        return InviteCreateResponse.builder()
                .inviterId(savedInvite.getInviterId())
                .challengeId(savedInvite.getChallengeId())
                .inviteeId(savedInvite.getInviteeId())
                .message(com.likelion.devroutine.invite.enumerate.ResponseMessage.INVITE_SUCCESS.getMessage())
                .build();
    }

    public List<FollowerResponse> findFollowers(String oauthId, Long challengeId) {
        User user = getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        matchWriterAndUser(user, challenge);
        List<Follow> followers=followRepository.findByFollowerId(user.getId());
        return FollowerResponse.of(followers);
    }

    public ParticipationChallengeDto findByParticipateChallenge(String oauthId, Long challengeId){
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        Participation participation =getParticipant(user, challenge);
        List<ParticipationSortResponse> participations = participationRepository.findAllByChallengeOrderByCertificationCnt(challenge);
        Map<String, List<CertificationResponse>> certificationResponses=getCertification(participations);
        return ParticipationChallengeDto.toResponse(participation, ChallengeHashTagResponse.of(challenge.getChallengeHashTags()), certificationResponses);
    }
    public Map<String, List<CertificationResponse>> getCertification(List<ParticipationSortResponse> participations){
        return participations.stream()
                .collect(Collectors.toMap(
                        participation -> participation.getUsername(),
                        participation -> CertificationResponse.of(certificationRepository.findByParticipationId(participation.getParticipationId()))
                ));
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

    private void validateFollower(User inviter, User invitee) {
        Follow follow=followRepository.findByFollowerIdAndFollowingId(inviter.getId(), invitee.getId())
                .orElseThrow(()->new FollowingNotFoundException());
    }

    private boolean matchWriterAndUser(User user, Challenge challenge) {
        if (!user.getId().equals(challenge.getUserId())) {
            throw new InvalidPermissionException();
        }
        return true;
    }

    public List<ChallengeDto> findAllParticipateChallenge(String oauthId){
        User user=getUser(oauthId);
        List<Participation> participations=participationRepository.findAllByUserId(user.getId());
        List<Challenge> participateChallenges=participations.stream()
                .filter(participation -> challengeRepository.findById(participation.getChallenge().getId()).isPresent())
                .map(participation -> challengeRepository.findById(participation.getChallenge().getId()).get())
                .collect(Collectors.toList());
        return ChallengeDto.toList(participateChallenges, getChallengeHashTagResponse(participateChallenges));
    }

    private Map<Long, List<ChallengeHashTagResponse>> getChallengeHashTagResponse(List<Challenge> challenges){
        return challenges
                .stream()
                .collect(Collectors.toMap(
                        challenge-> challenge.getId(),
                        challenge->ChallengeHashTagResponse.of(getHashTags(challenge.getId()))
                ));
    }

    private List<ChallengeHashTag> getHashTags(Long challengeId) {
        return challengeHashTagRepository.findByChallengeId(challengeId);
    }

    public void saveInviteAlarm(User user, Long inviterId) {
        alarmRepository.save(Alarm.createAlarm(inviterId,
                AlarmType.NEW_CHALLENGE_INVITE,AlarmType.NEW_CHALLENGE_INVITE.getMessage(), user));
    }
}
