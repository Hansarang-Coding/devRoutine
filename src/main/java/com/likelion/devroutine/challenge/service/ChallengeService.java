package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import org.springframework.stereotype.Service;

@Service
public class ChallengeService {
    private final ChallengeRepository challengeRepository;

    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }
}
