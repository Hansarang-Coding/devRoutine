package com.likelion.devroutine.invite.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class InviteResponse {
    private Long challengeId;
    private String message;
}
