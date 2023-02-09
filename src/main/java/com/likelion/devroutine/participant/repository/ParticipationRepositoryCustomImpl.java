package com.likelion.devroutine.participant.repository;

import com.likelion.devroutine.certification.domain.QCertification;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.domain.QParticipation;
import com.likelion.devroutine.participant.dto.ParticipationSortResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.likelion.devroutine.participant.domain.QParticipation.participation;

@RequiredArgsConstructor
public class ParticipationRepositoryCustomImpl implements ParticipationRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ParticipationSortResponse> findAllByChallengeOrderByCertificationCnt(Challenge challenge) {
        NumberPath<Long> aliasQuantity= Expressions.numberPath(Long.class, "cnt");
        QParticipation participation=QParticipation.participation;
        QCertification certification=QCertification.certification;
        return queryFactory.select(Projections.constructor(ParticipationSortResponse.class, certification.count().as(aliasQuantity), participation.id,
                        participation.challenge.id, participation.user.name))
                .from(participation)
                .leftJoin(certification).on(certification.participation.id.eq(participation.id))
                .where(participation.challenge.id.eq(challenge.getId()))
                .groupBy(participation.id)
                .orderBy(aliasQuantity.desc(), participation.user.name.asc())
                .fetch();
    }
}
