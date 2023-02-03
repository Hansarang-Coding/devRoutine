package com.likelion.devroutine.invite.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.invite.dto.InviteeResponse;

import java.util.List;

public interface InviteRepositoryCustom {
    List<Challenge> findInviterByInviteeId(Long inviterId);
    List<InviteeResponse> findInviteeByInviterId(Long inviteeId);
}
