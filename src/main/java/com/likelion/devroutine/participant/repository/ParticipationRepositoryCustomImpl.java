package com.likelion.devroutine.participant.repository;

import com.likelion.devroutine.certification.domain.QCertification;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.domain.QChallenge;
import com.likelion.devroutine.participant.domain.QParticipation;
import com.likelion.devroutine.participant.dto.ParticipationSortResponse;
import com.likelion.devroutine.participant.dto.PopularParticipationResponse;
import com.likelion.devroutine.participant.domain.Participation;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.likelion.devroutine.challenge.domain.QChallenge.challenge;
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

    @Override
    public List<PopularParticipationResponse> findPopularParticipation() {
        QParticipation participation=QParticipation.participation;
        QChallenge challenge=QChallenge.challenge;
        return queryFactory.select(Projections.constructor(PopularParticipationResponse.class,

                        participation.challenge.id.count(), challenge.id, challenge.vigibility, challenge.title,
                        challenge.description, challenge.startDate, challenge.endDate))
                .from(participation)
                .rightJoin(challenge).on(participation.challenge.id.eq(challenge.id))
                .where(challenge.startDate.after(LocalDate.now()), challenge.vigibility.eq(true))
                .groupBy(challenge.id)
                .orderBy(participation.challenge.id.count().desc())
                .limit(3)
                .fetch();
    }

    @Override
    public List<Participation> findAllFinishParticipation(Long userId){
        QParticipation participation=QParticipation.participation;
        QChallenge challenge= QChallenge.challenge;

        return queryFactory.selectFrom(participation)
                .join(participation.challenge, challenge)
                .fetchJoin()
                .where(participation.user.id.eq(userId), challenge.endDate.before(LocalDate.now()))
                .fetch();
    }

    @Override
    public List<Participation> findAllByUserId(Long userId) {
        return queryFactory.selectFrom(participation)
                .join(participation.challenge,challenge)
                .fetchJoin()
                .where(participation.user.id.eq(userId),challenge.endDate.after(LocalDate.now()))
                .orderBy(challenge.startDate.asc())
                .fetch();
    }

    @Override
    public List<Participation> findProgressingChallengeByUserId(Long userId) {
        return queryFactory.selectFrom(participation)
                .join(participation.challenge,challenge)
                .fetchJoin()
                .where(participation.user.id.eq(userId),challenge.endDate.after(LocalDate.now().minusDays(1)),challenge.startDate.before(LocalDate.now().plusDays(1)))
                .fetch();
    }
}
