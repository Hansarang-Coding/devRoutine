package com.likelion.devroutine.participant.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
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

    public ParticipationService(ParticipationRepository participationRepository, UserRepository userRepository, ChallengeRepository challengeRepository, InviteRepository inviteRepository) {
        this.participationRepository = participationRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.inviteRepository = inviteRepository;
    }
    @Transactional
    public ParticipationResponse participateChallenge(String oauthId, Long challengeId) {
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        validateParticipate(user, challenge);
        Participation savedParticipation = participationRepository.save(Participation.createParticipant(user, challenge));
        return ParticipationResponse.builder()
                .challengeId(savedParticipation.getChallenge().getId())
                .message(ResponseMessage.PARTICIPATE_SUCCESS.getMessage())
                .build();
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

    public boolean validateDuplicateParticipate(User user, Challenge challenge){
        participationRepository.findByUserAndChallenge(user, challenge)
                .ifPresent(participant->{
                    throw new DuplicatedParticipationException();
                });
        return true;
    }

    public boolean validateParticipate(User user, Challenge challenge){
        isViewable(challenge, user);
        isProgressChallenge(challenge.getStartDate());
        validateDuplicateParticipate(user, challenge);
        return true;
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
}
