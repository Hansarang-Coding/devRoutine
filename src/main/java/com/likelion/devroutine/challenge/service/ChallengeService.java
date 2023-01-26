package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.dto.ChallengeCreateResponse;
import com.likelion.devroutine.challenge.dto.ChallengeResponse;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
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

    public List<ChallengeResponse> findAllChallenge(Long challengeId, int size) {
        List<Challenge> challenges=challengeRepository.findAllSortById(challengeId, PageRequest.of(0, size));

        return ChallengeResponse.toList(challenges);
    }

    public List<ChallengeResponse> findAllChallengeTitle(Long challengeId, int size, String keyword) {
        List<Challenge> challenges = challengeRepository.findSearchTitleSortById(challengeId, keyword, PageRequest.of(0, size));
        List<ChallengeResponse> challengeResponses=ChallengeResponse.toList(challenges);
        return challengeResponses;
    }

    public ChallengeResponse findByChallengeId(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException());
        isVigibility(challenge);
        return ChallengeResponse.toResponse(challenge);
    }

    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest dto) {
        //userId 존재하지 않으면 USER_NOT_FOUND 예외처리

        //사용자 id 추가(FK)
        Challenge savedChallenge=challengeRepository.save(Challenge.createChallenge(dto));
        return ChallengeCreateResponse.toResponse(savedChallenge);
    }
    public boolean isVigibility(Challenge challenge){
        if(challenge.getIsVigibility()) return true;
        else throw new InaccessibleChallengeException();
    }


}
