package com.likelion.devroutine.alarm.domain;

import com.likelion.devroutine.alarm.enumurate.AlarmType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    private String message;
    private Long challenge_id;
    private Long comment_id;
    private Long like_id;
    private Long user_id;

    @CreatedDate
    private LocalDateTime createdAt;

}
