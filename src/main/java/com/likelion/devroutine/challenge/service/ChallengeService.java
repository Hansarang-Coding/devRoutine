package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.exception.UserNotFoundException;
import com.likelion.devroutine.auth.repository.UserRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.enumerate.ResponseMessage;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly=true)
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    public ChallengeService(ChallengeRepository challengeRepository, UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }

    public List<ChallengeDto> findAllChallenge(Long challengeId, int size) {
        List<Challenge> challenges=challengeRepository.findAllSortById(challengeId, PageRequest.of(0, size));

        return ChallengeDto.toList(challenges);
    }

    public List<ChallengeDto> findAllChallengeTitle(Long challengeId, int size, String keyword) {
        List<Challenge> challenges = challengeRepository.findSearchTitleSortById(challengeId, keyword, PageRequest.of(0, size));
        List<ChallengeDto> challengeDtos= ChallengeDto.toList(challenges);
        return challengeDtos;
    }

    public ChallengeDto findByChallengeId(Long id) {
        Challenge challenge = getChallenge(id);
        isVigibility(challenge);
        return ChallengeDto.toDto(challenge);
    }

    @Transactional
    public ChallengeCreateResponse createChallenge(String oauthId, ChallengeCreateRequest dto) {
        User user=getUser(oauthId);

        Challenge savedChallenge=challengeRepository.save(Challenge.createChallenge(user, dto));
        return ChallengeCreateResponse.toResponse(savedChallenge);
    }

    @Transactional
    public ChallengeResponse deleteChallenge(String oauthId, Long id) {
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(id);

        matchWriterAndUser(getChallenge(id), getUser(oauthId));

        //챌린지 시작 전인지 확인
        isProgressChallenge(challenge.getStartDate());

        challenge.deleteChallenge();
        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_DELETE_SUCCESS.getMessage()).build();
    }

    @Transactional
    public ChallengeResponse modifyChallenge(String oauthId, Long id, ChallengeModifiyRequest dto) {
        User user=getUser(oauthId);
        Challenge challenge=getChallenge(id);

        matchWriterAndUser(getChallenge(id), getUser(oauthId));

        isProgressChallenge(challenge.getStartDate());
        challenge.updateChallenge(dto);
        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_MODIFY_SUCCESS.getMessage()).build();
    }

    public Challenge getChallenge(Long id){
        Challenge challenge=challengeRepository.findById(id)
                .orElseThrow(()->new ChallengeNotFoundException());
        return challenge;
    }

    public boolean isVigibility(Challenge challenge){
        if(challenge.getVigibility()) return true;
        else throw new InaccessibleChallengeException();
    }

    public boolean isProgressChallenge(LocalDate startDate){
        if(LocalDate.now().isAfter(startDate))
            throw new InProgressingChallengeException();
        return false;
    }
    public User getUser(String oauthId){
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(()->new UserNotFoundException());

        return user;
    }

    public boolean matchWriterAndUser(Challenge challenge, User user) {
        if(!user.getId().equals(challenge.getUser().getId())){
            throw new InvalidPermissionException();
        }
        return true;
    }
}
