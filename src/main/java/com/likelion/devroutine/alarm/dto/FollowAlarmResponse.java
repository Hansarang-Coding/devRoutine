package com.likelion.devroutine.alarm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FollowAlarmResponse {
    private Long alarmId;
    private Long userId;
    private String userName;
    private String userImage;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/dd/mm hh:mm:ss")
    private LocalDateTime createdAt;

    @QueryProjection
    public FollowAlarmResponse(Long alarmId, Long userId, String userName, String userImage, LocalDateTime createdAt){
        this.alarmId=alarmId;
        this.userId=userId;
        this.userName=userName;
        this.userImage=userImage;
        this.createdAt=createdAt;
    }
}
