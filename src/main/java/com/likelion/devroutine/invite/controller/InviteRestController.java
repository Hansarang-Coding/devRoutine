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
@RequestMapping("/api/v1")
@Slf4j
public class InviteRestController {
    private final InviteService inviteService;

    public InviteRestController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @GetMapping("/challenges/{challengeId}/invites")
    public ResponseEntity<List<FollowerResponse>> getFollowList(Authentication authentication, @PathVariable Long challengeId){
        List<FollowerResponse> followerResponses = inviteService.findFollowers(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(followerResponses);
    }
    @PostMapping("/challenges/{challengeId}/invites/{userId}")
    public ResponseEntity<InviteCreateResponse> inviteUser(Authentication authentication, @PathVariable Long challengeId, @PathVariable Long userId){
        InviteCreateResponse inviteCreateResponse =inviteService.inviteUser(authentication.getName(), challengeId, userId);
        return ResponseEntity.ok().body(inviteCreateResponse);
    }
    @PostMapping("/invites/{id}")
    public ResponseEntity<InviteResponse> acceptInvite(Authentication authentication, @PathVariable Long id){
        InviteResponse inviteResponse =inviteService.acceptInvite(authentication.getName(), id);
        return ResponseEntity.ok().body(inviteResponse);
    }

    @GetMapping("/invitees") //로그인한 유저=invitee
    public ResponseEntity<List<InviterResponse>> findInviters(Authentication authentication){
        List<InviterResponse> inviterResponses=inviteService.findInviters(authentication.getName());
        return ResponseEntity.ok().body(inviterResponses);
    }

    @GetMapping("/inviters") //로그인한 유저=inviter
    public ResponseEntity<List<InviteeResponse>> findInvitees(Authentication authentication){
        List<InviteeResponse> inviteeResponses=inviteService.findInvitees(authentication.getName());
        return ResponseEntity.ok().body(inviteeResponses);
    }

    @DeleteMapping("/challenges/{challengeId}/reject")
    public ResponseEntity<InviteResponse> rejectInvite(Authentication authentication, @PathVariable Long challengeId){
        InviteResponse inviteResponse =inviteService.rejectInvite(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(inviteResponse);

    }

    @DeleteMapping("/invites/{inviteId}/cancel")
    public ResponseEntity<InviteCancelResponse> cancelInvite(Authentication authentication, @PathVariable Long inviteId){
        InviteCancelResponse inviteCancelResponse=inviteService.cancelInvite(authentication.getName(), inviteId);
        return ResponseEntity.ok().body(inviteCancelResponse);
    }
}
