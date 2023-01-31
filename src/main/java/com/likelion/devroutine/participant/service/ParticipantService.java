package com.likelion.devroutine.participant.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.participant.domain.Participant;
import com.likelion.devroutine.participant.dto.ParticipateChallengeResponse;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.enumerate.ResponseMessage;
import com.likelion.devroutine.participant.exception.DuplicatedChallengeException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.participant.exception.ParticipantNotFoundException;
import com.likelion.devroutine.participant.repository.ParticipantRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    public ParticipantService(ParticipantRepository participantRepository, UserRepository userRepository, ChallengeRepository challengeRepository) {
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
    }
    @Transactional
    public ParticipationResponse participateChallenge(String oauthId, Long challengeId) {
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        validateParticipate(user, challenge);
        Participant savedParticipant=participantRepository.save(Participant.createParticipant(user, challenge));
        return ParticipationResponse.builder()
                .challengeId(savedParticipant.getChallenge().getId())
                .message(ResponseMessage.PARTICIPATE_SUCCESS.getMessage())
                .build();
    }

    @Transactional
    public ParticipationResponse cancelChallenge(String oauthId, Long challengeId) {
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        Participant participant=getParticipant(user, challenge);
        isProgressChallenge(challenge.getStartDate());
        participantRepository.delete(participant);
        return ParticipationResponse.builder()
                .challengeId(challenge.getId())
                .message(ResponseMessage.CHALLENGE_CANCEL_SUCCESS.getMessage())
                .build();
    }
    public ParticipateChallengeResponse findByParticipateChallenge(String oauthId, Long challengeId){
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        Participant participant=getParticipant(user, challenge);
        List<Participant> participants=participantRepository.findAllByChallenge(challenge);
        return ParticipateChallengeResponse.toResponse(participant, participants);
    }
    public Participant getParticipant(User user, Challenge challenge){
        return participantRepository.findByUserAndChallenge(user, challenge)
                .orElseThrow(()-> new ParticipantNotFoundException());
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
        participantRepository.findByUserAndChallenge(user, challenge)
                .ifPresent(participant->{
                    throw new DuplicatedChallengeException();
                });
        return true;
    }

    public boolean validateParticipate(User user, Challenge challenge){
        isVigibility(challenge);
        isProgressChallenge(challenge.getStartDate());
        validateDuplicateParticipate(user, challenge);
        return true;
    }

    public boolean isVigibility(Challenge challenge) {
        if (challenge.getVigibility()) return true;
        else throw new InaccessibleChallengeException();
    }

    public boolean isProgressChallenge(LocalDate startDate) {
        if (LocalDate.now().isAfter(startDate))
            throw new InProgressingChallengeException();
        return false;
    }
}
