package com.likelion.devroutine.invite.controller;

import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.dto.FollowingResponse;
import com.likelion.devroutine.invite.dto.InviteAcceptResponse;
import com.likelion.devroutine.invite.dto.InviteResponse;
import com.likelion.devroutine.invite.dto.InviteeResponse;
import com.likelion.devroutine.invite.dto.InviterResponse;
import com.likelion.devroutine.invite.service.InviteService;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
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
    public ResponseEntity<InviteResponse> inviteUser(Authentication authentication, @PathVariable Long challengeId, @PathVariable Long userId){
        InviteResponse inviteResponse=inviteService.inviteUser(authentication.getName(), challengeId, userId);
        return ResponseEntity.ok().body(inviteResponse);
    }
    @PostMapping("/invites/{id}")
    public ResponseEntity<InviteAcceptResponse> acceptInvite(Authentication authentication, @PathVariable Long id){
        InviteAcceptResponse inviteAcceptResponse=inviteService.acceptInvite(authentication.getName(), id);
        return ResponseEntity.ok().body(inviteAcceptResponse);
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
}
