package com.likelion.devroutine.invite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
public class InviteReadResponse {
    private List<InviterResponse> inviterResponse;
    private List<InviteeResponse> inviteeResponses;
}
