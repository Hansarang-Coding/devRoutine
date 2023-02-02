package com.likelion.devroutine.invite.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.domain.QChallenge;
import com.likelion.devroutine.invite.dto.InviteeResponse;
import com.likelion.devroutine.user.domain.QUser;
import com.querydsl.core.Query;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.likelion.devroutine.challenge.domain.QChallenge.challenge;
import static com.likelion.devroutine.invite.domain.QInvite.invite;
import static com.likelion.devroutine.user.domain.QUser.user;

@RequiredArgsConstructor
public class InviteRepositoryCustomImpl implements InviteRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Challenge> findInviterByInviteeId(Long userId){
        return queryFactory.select(challenge)
                .from(invite)
                .join(challenge).on(invite.challengeId.eq(challenge.id))
                .where(invite.challengeId.eq(challenge.id), invite.inviteeId.eq(userId))
                .fetch();
    }

    @Override
    public List<InviteeResponse> findInviteeByInviterId(Long userId){
        QChallenge challenge=QChallenge.challenge;
        QUser user= QUser.user;
        return queryFactory.select(Projections.constructor(InviteeResponse.class, challenge.id, challenge.title, user.name, user.picture))
                .from(invite)
                .join(challenge).on(invite.challengeId.eq(challenge.id))
                .join(user).on(invite.inviteeId.eq(user.id))
                .where(invite.inviterId.eq(userId))
                .fetch();
    }
}
