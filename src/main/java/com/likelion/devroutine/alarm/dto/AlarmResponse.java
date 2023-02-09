package com.likelion.devroutine.alarm.dto;

import com.likelion.devroutine.alarm.domain.Alarm;
import com.likelion.devroutine.alarm.enumurate.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class AlarmResponse {

    //알림의 id값
    private Long id;
    private AlarmType alarmType;
    private String message;

    //알림을 받는 사람
    private Long userId;


    private LocalDateTime createdAt;

    public static List<AlarmResponse> of(List<Alarm> alarms) {
        return alarms.stream()
                .map(alarm -> AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .message(alarm.getMessage())
                .userId(alarm.getUser().getId())
                .createdAt(alarm.getCreatedAt())
                .build())
                .collect(Collectors.toList());
    }
//    public static List<AlarmResponse> toList(List<Alarm> alarms) {
//        return alarms.stream()
//                .map(alarm -> AlarmResponse.builder()
//                        .id(alarm.getId())
//                        .alarmType(alarm.getAlarmType())
//                        .message(alarm.getMessage())
//                        .userId(alarm.getUser().getId())
//                        .createdAt(alarm.getCreatedAt())
//                        .build())
//                .collect(Collectors.toList());
//    }

}
