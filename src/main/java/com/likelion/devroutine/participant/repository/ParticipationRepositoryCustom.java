package com.likelion.devroutine.participant.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.dto.ParticipationSortResponse;
import com.likelion.devroutine.participant.dto.PopularParticipationResponse;

import java.util.List;

public interface ParticipationRepositoryCustom {
    List<ParticipationSortResponse> findAllByChallengeOrderByCertificationCnt(Challenge challenge);

    List<PopularParticipationResponse> findPopularParticipation();
    List<Participation> findAllFinishParticipation(Long userId);
    List<Participation> findAllByUserId(Long userId);
    List<Participation> findProgressingChallengeByUserId(Long userId);
}
