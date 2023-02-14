package com.likelion.devroutine.invite.repository;

import com.likelion.devroutine.invite.dto.InviteeResponse;
import com.likelion.devroutine.invite.dto.InviterResponse;

import java.util.List;

public interface InviteRepositoryCustom {
    List<InviterResponse> findInviterByInviteeId(Long inviterId);
    List<InviteeResponse> findInviteeByInviterId(Long inviteeId);
}
