package com.likelion.devroutine.alarm.repository;

import com.likelion.devroutine.alarm.dto.CertificationAlarmResponse;
import com.likelion.devroutine.alarm.dto.FollowAlarmResponse;
import com.likelion.devroutine.alarm.enumurate.AlarmType;
import com.likelion.devroutine.certification.domain.QCertification;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.likelion.devroutine.alarm.domain.QAlarm.alarm;
import static com.likelion.devroutine.user.domain.QUser.user;

@RequiredArgsConstructor
public class AlarmRepositoryCustomImpl implements AlarmRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CertificationAlarmResponse> findLikeAlarmByUser(Long userId) {
        return queryFactory.select(Projections.constructor(CertificationAlarmResponse.class, alarm.id, user.id, user.name, user.picture, alarm.targetId, alarm.createdAt))
                .from(alarm)
                .leftJoin(user).on(user.id.eq(alarm.fromUserId))
                .where(alarm.alarmType.eq(AlarmType.NEW_LIKE_ON_CERTIFICATION), alarm.user.id.eq(userId), alarm.createdAt.after(LocalDateTime.now().minusWeeks(2)))
                .orderBy(alarm.createdAt.desc())
                .fetch();
    }

    @Override
    public List<CertificationAlarmResponse> findCommentAlarmByUser(Long userId) {
        return queryFactory.select(Projections.constructor(CertificationAlarmResponse.class, alarm.id, user.id, user.name, user.picture, alarm.targetId, alarm.createdAt))
                .from(alarm)
                .leftJoin(user).on(user.id.eq(alarm.fromUserId))
                .where(alarm.alarmType.eq(AlarmType.NEW_COMMENT_ON_CERTIFICATION), alarm.user.id.eq(userId), alarm.createdAt.after(LocalDateTime.now().minusWeeks(2)))
                .orderBy(alarm.createdAt.desc())
                .fetch();
    }

    @Override
    public List<FollowAlarmResponse> findFollowAlarmByUser(Long userId) {
        return queryFactory.select(Projections.constructor(FollowAlarmResponse.class, alarm.id, user.id, user.name, user.picture, alarm.createdAt))
                .from(alarm)
                .leftJoin(user).on(user.id.eq(alarm.fromUserId))
                .where(alarm.alarmType.eq(AlarmType.NEW_FOLLOW), alarm.user.id.eq(userId), alarm.createdAt.after(LocalDateTime.now().minusWeeks(2)))
                .orderBy(alarm.createdAt.desc())
                .fetch();
    }
}
