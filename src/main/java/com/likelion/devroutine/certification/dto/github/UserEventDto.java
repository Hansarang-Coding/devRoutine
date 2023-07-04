package com.likelion.devroutine.certification.dto.github;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@ToString
@NoArgsConstructor
public class UserEventDto {
    private LocalDateTime created_at;
    private String type;
    private PayloadDto payload;
}
