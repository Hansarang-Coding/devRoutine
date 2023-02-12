package com.likelion.devroutine.alarm.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class FollowAlarmResponse {
    private Long alarmId;
    private Long userId;
    private String userName;
    private String userImage;

    @QueryProjection
    public FollowAlarmResponse(Long alarmId, Long userId, String userName, String userImage){
        this.alarmId=alarmId;
        this.userId=userId;
        this.userName=userName;
        this.userImage=userImage;
    }
}
