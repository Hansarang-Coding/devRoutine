package com.likelion.devroutine.invite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InviteCreateResponse {
    private Long inviterId;
    private Long challengeId;
    private Long inviteeId;
    private String message;
}
