package com.likelion.devroutine.invite.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class InviteAcceptResponse {
    private Long challengeId;
    private String message;
}
