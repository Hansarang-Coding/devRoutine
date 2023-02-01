package com.likelion.devroutine.invite.controller;

import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.dto.FollowingResponse;
import com.likelion.devroutine.invite.dto.InviteAcceptResponse;
import com.likelion.devroutine.invite.dto.InviteResponse;
import com.likelion.devroutine.invite.service.InviteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InviteRestController {
    private final InviteService inviteService;

    public InviteRestController(InviteService inviteService) {
        this.inviteService = inviteService;
    }

    @GetMapping("/challenges/{challengeId}/invites")
    public ResponseEntity<List<FollowerResponse>> getFollowList(Authentication authentication, @PathVariable Long challengeId){
        List<FollowerResponse> followerResponses = inviteService.getFollowList(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(followerResponses);
    }
    @PostMapping("/challenges/{challengeId}/invites/{userId}")
    public ResponseEntity<InviteResponse> inviteUser(Authentication authentication, @PathVariable Long challengeId, @PathVariable Long userId){
        InviteResponse inviteResponse=inviteService.inviteUser(authentication.getName(), challengeId, userId);
        return ResponseEntity.ok().body(inviteResponse);
    }
}
