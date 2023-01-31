package com.likelion.devroutine.participant.controller;

import com.likelion.devroutine.participant.dto.ParticipateChallengeResponse;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.service.ParticipantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/challenges/")
@Slf4j
public class ParticipantRestController {
    private final ParticipantService participantService;

    public ParticipantRestController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @PostMapping("/{challengeId}")
    public ResponseEntity<ParticipationResponse> participateChallenge(Authentication authentication, @PathVariable Long challengeId){
        ParticipationResponse participationResponse=participantService.participateChallenge(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(participationResponse);
    }

    @DeleteMapping("/{challengeId}/participant")
    public ResponseEntity<ParticipationResponse> cancelChallenge(Authentication authentication, @PathVariable Long challengeId){
        ParticipationResponse participationResponse=participantService.cancelChallenge(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(participationResponse);
    }

    @GetMapping("/{challengeId}/participant")
    public ResponseEntity<ParticipateChallengeResponse> findByParticipateChallenge(Authentication authentication, @PathVariable Long challengeId){
        ParticipateChallengeResponse participateChallengeResponse=participantService.findByParticipateChallenge(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(participateChallengeResponse);
    }
}
