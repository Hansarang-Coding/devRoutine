package com.likelion.devroutine.participant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ParticipationResponse {
    private Long challengeId;
    private String message;
}
