package com.likelion.devroutine.challenge.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.domain.QChallenge;
import com.likelion.devroutine.hashtag.domain.QChallengeHashTag;
import com.likelion.devroutine.hashtag.domain.QHashTag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static com.likelion.devroutine.challenge.domain.QChallenge.challenge;

@RequiredArgsConstructor
public class ChallengeRepositoryCustomImpl implements ChallengeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Challenge> findSearchTitleSortById(String keyword) {
        QChallengeHashTag challengehashtag=QChallengeHashTag.challengeHashTag;
        QChallenge challenge=QChallenge.challenge;
        QHashTag hashTag= QHashTag.hashTag;
        return queryFactory.selectFrom(challenge)
                .join(challenge.challengeHashTags, challengehashtag)
                .fetchJoin()
                .join(challengehashtag.hashTag, hashTag)
                .fetchJoin()
                .where(challenge.title.contains(keyword).or(hashTag.contents.eq(keyword)), challenge.vigibility.eq(true), challenge.startDate.after(LocalDate.now()))
                .orderBy(challenge.id.desc())
                .fetch();

    }

    @Override
    public List<Challenge> findAllSortById() {
        return queryFactory.selectFrom(challenge)
                .where(challenge.vigibility.eq(true), challenge.startDate.after(LocalDate.now()))
                .orderBy(challenge.id.desc())
                .fetch();
    }


    /*
    첫페이지에 접근한 경우 Null
    n번째 페이지에 접근한 경우 challenge id<cursorId
    */
    private BooleanExpression cursorId(Long cursorId){
        return cursorId == null ? null : challenge.id.lt(cursorId); //lt : challenge.id < cursorId
    }
}
