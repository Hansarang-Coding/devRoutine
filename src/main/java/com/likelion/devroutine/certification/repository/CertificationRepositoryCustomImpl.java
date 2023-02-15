package com.likelion.devroutine.certification.repository;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.domain.QCertification;
import com.likelion.devroutine.challenge.domain.QChallenge;
import com.likelion.devroutine.participant.domain.QParticipation;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@RequiredArgsConstructor
public class CertificationRepositoryCustomImpl implements CertificationRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Certification> findAllCertification() {
        QCertification certification=QCertification.certification;
        QParticipation participation=QParticipation.participation;
        QChallenge challenge=QChallenge.challenge;
        //@Query("select c from Certification c order by c.createdAt desc")
        return queryFactory.selectFrom(certification)
                .join(certification.participation, participation)
                .fetchJoin()
                .join(participation.challenge, challenge)
                .fetchJoin()
                .where(challenge.vigibility.eq(true))
                .orderBy(certification.createdAt.desc())
                .fetch();
    }
}
