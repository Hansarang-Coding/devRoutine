package com.likelion.devroutine.alarm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class CertificationAlarmResponse {
    private Long alarmId;
    private Long userId;
    private String userImage;
    private String userName;
    private Long certificationId;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/dd/mm hh:mm:ss")
    private LocalDateTime createdAt;

    @QueryProjection
    public CertificationAlarmResponse(Long alarmId, Long userId, String userName, String userImage, Long certificatoinId, LocalDateTime createdAt){
        this.alarmId=alarmId;
        this.userId=userId;
        this.userName=userName;
        this.userImage=userImage;
        this.certificationId=certificatoinId;
        this.createdAt=createdAt;
    }
}
