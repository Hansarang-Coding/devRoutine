package com.likelion.devroutine.invite.controller;

import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.invite.dto.*;
import com.likelion.devroutine.invite.service.InviteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invites")
@Slf4j
public class InviteRestController {
    private final InviteService inviteService;

    public InviteRestController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @PostMapping("/{inviteId}")
    public ResponseEntity<InviteResponse> acceptInvite(Authentication authentication, @PathVariable Long inviteId){
        InviteResponse inviteResponse =inviteService.acceptInvite(authentication.getName(), inviteId);
        return ResponseEntity.ok().body(inviteResponse);
    }

    @GetMapping("")
    public ResponseEntity<InviteReadResponse> findAllInvite(Authentication authentication){
        InviteReadResponse inviteReadResponses=inviteService.findAllInvite(authentication.getName());
        return ResponseEntity.ok().body(inviteReadResponses);
    }

    //로직 좀 수정하기
    @DeleteMapping("/{inviteId}/reject")
    public ResponseEntity<InviteCancelResponse> rejectInvite(Authentication authentication, @PathVariable Long inviteId){
        InviteCancelResponse inviteRejectResponse =inviteService.rejectInvite(authentication.getName(), inviteId);
        return ResponseEntity.ok().body(inviteRejectResponse);

    }

    @DeleteMapping("/{inviteId}/cancel")
    public ResponseEntity<InviteCancelResponse> cancelInvite(Authentication authentication, @PathVariable Long inviteId){
        InviteCancelResponse inviteCancelResponse=inviteService.cancelInvite(authentication.getName(), inviteId);
        return ResponseEntity.ok().body(inviteCancelResponse);
    }
}
