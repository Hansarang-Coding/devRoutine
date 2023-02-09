package com.likelion.devroutine.participant.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.dto.ParticipationSortResponse;

import java.util.List;

public interface ParticipationRepositoryCustom {
    List<ParticipationSortResponse> findAllByChallengeOrderByCertificationCnt(Challenge challenge);
}