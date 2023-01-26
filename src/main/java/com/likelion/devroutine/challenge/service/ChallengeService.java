package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.enumerate.ResponseMessage;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
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
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest dto) {
        //userId 존재하지 않으면 USER_NOT_FOUND 예외처리

        //사용자 id 추가(FK)
        //기간 추가
        Challenge savedChallenge=challengeRepository.save(Challenge.createChallenge(dto));
        return ChallengeCreateResponse.toResponse(savedChallenge);
    }

    @Transactional
    public ChallengeResponse deleteChallenge(Long id) {
        //로그인한 User 받아오기
        //로그인한 User가 생성한 challenge인지 확인

        Challenge challenge=getChallenge(id);
        //챌린지 시작 전인지 확인

        challenge.deleteChallenge();
        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_DELETE_SUCCESS.getMessage()).build();
    }

    @Transactional
    public ChallengeResponse modifyChallenge(Long id, ChallengeModifiyRequest dto) {
        //로그인한 User 받아오기
        //로그인한 User가 생성한 challenge인지 확인

        Challenge challenge=getChallenge(id);
        //챌린지 시작 전인지 확인

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
        if(challenge.getIsVigibility()) return true;
        else throw new InaccessibleChallengeException();
    }
}
