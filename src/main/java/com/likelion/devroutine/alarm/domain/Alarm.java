package com.likelion.devroutine.alarm.domain;

import com.likelion.devroutine.alarm.enumurate.AlarmType;
import com.likelion.devroutine.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Entity
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    private String message;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    private LocalDateTime createdAt;

    public static Alarm createAlarm(
            Long targetId,AlarmType alarmType, String message, User user) {
        return Alarm.builder()
                .targetId(targetId)
                .alarmType(alarmType)
                .message(message)
                .user(user)
                .build();
    }
}
