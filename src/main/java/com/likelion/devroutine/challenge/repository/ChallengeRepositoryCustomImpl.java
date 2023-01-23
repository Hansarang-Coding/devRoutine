package com.likelion.devroutine.challenge.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.likelion.devroutine.challenge.domain.QChallenge.challenge;

@RequiredArgsConstructor
public class ChallengeRepositoryCustomImpl implements ChallengeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Challenge> findSearchTitleSortById(Long cursorId, String keyword, Pageable pageable) {
        return queryFactory.selectFrom(challenge)
                .where(cursorId(cursorId), challenge.title.contains(keyword))
                .orderBy(challenge.id.desc())
                .limit(pageable.getPageSize())
                .fetch();

    }

    @Override
    public List<Challenge> findAllSortById(Long cursorId, Pageable pageable) {
        return queryFactory.selectFrom(challenge)
                .where(cursorId(cursorId))
                .orderBy(challenge.id.desc())
                .limit(pageable.getPageSize())
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
