package com.likelion.devroutine.alarm.repository;

import com.likelion.devroutine.alarm.dto.CertificationAlarmResponse;
import com.likelion.devroutine.alarm.dto.FollowAlarmResponse;

import java.util.List;

public interface AlarmRepositoryCustom {
    List<CertificationAlarmResponse> findLikeAlarmByUser(Long userId);
    List<CertificationAlarmResponse> findCommentAlarmByUser(Long userId);
    List<FollowAlarmResponse> findFollowAlarmByUser(Long userId);
}
