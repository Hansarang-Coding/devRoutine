package com.likelion.devroutine.alarm.dto;

import com.likelion.devroutine.alarm.domain.Alarm;
import com.likelion.devroutine.alarm.enumurate.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AlarmResponse {
    private Long id;
    private AlarmType alarmType;
    private Long fromUserId;
    private Long targetId;
    private String message;
    private LocalDateTime createdAt;

    public static Page<AlarmResponse> of(Page<Alarm> alarms) {
        return alarms.map(alarm -> AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getTargetId())
                .text(alarm.getText())
                .createdAt(alarm.getCreatedAt())
                .build());
    }
}
