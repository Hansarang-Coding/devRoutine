package com.likelion.devroutine.invite.dto;

import com.likelion.devroutine.invite.domain.Invite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InviteResponse {
    private Long inviterId;
    private Long challengeId;
    private Long inviteeId;
    private String message;
}
