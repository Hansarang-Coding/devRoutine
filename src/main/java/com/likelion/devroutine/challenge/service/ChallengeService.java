package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.ChallengeResponse;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    public List<ChallengeResponse> findAllChallenge(Long challengeId, int size) {
        Slice<Challenge> challenges=challengeRepository.findByIdLessThanOrderByIdDesc(challengeId, PageRequest.of(0, size));

        return ChallengeResponse.toList(challenges);
    }

    public List<ChallengeResponse> findAllChallengeTitle(Long challengeId, int size, String keyword) {
        Slice<Challenge> challenges = challengeRepository.findByIdLessThanAndTitleContainingOrderByIdDesc(challengeId, keyword, PageRequest.of(0, size));
        List<ChallengeResponse> challengeResponses=ChallengeResponse.toList(challenges);
        return challengeResponses;
    }

    public ChallengeResponse findByChallengeId(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException());
        isVigibility(challenge);
        return ChallengeResponse.toResponse(challenge);
    }

    public boolean isVigibility(Challenge challenge){
        if(challenge.isVigibility()) return true;
        else throw new InaccessibleChallengeException();
    }
}
