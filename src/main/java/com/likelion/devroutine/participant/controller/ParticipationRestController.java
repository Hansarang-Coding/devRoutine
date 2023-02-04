package com.likelion.devroutine.participant.controller;

import com.likelion.devroutine.invite.dto.InviteCreateResponse;
import com.likelion.devroutine.participant.dto.ParticipantCertificationResponse;
import com.likelion.devroutine.participant.dto.ParticipationChallengeDto;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.service.ParticipationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/challenges/")
@Slf4j
public class ParticipationRestController {
    private final ParticipationService participationService;

    public ParticipationRestController(ParticipationService participationService) {
        this.participationService = participationService;
    }

    @PostMapping("/{challengeId}")
    public ResponseEntity<ParticipationResponse> participateChallenge(Authentication authentication, @PathVariable Long challengeId){
        ParticipationResponse participationResponse= participationService.participateChallenge(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(participationResponse);
    }

    @DeleteMapping("/{challengeId}/participation")
    public ResponseEntity<ParticipationResponse> cancelChallenge(Authentication authentication, @PathVariable Long challengeId){
        ParticipationResponse participationResponse= participationService.cancelChallenge(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(participationResponse);
    }

    @GetMapping("/{challengeId}/participation")
    public ResponseEntity<ParticipationChallengeDto> findByParticipateChallenge(Authentication authentication, @PathVariable Long challengeId){
        ParticipationChallengeDto participationChallengeDto = participationService.findByParticipateChallenge(authentication.getName(), challengeId);
        return ResponseEntity.ok().body(participationChallengeDto);
    }

    @PostMapping("/{challengeId}/invite/{userId}")
    public ResponseEntity<InviteCreateResponse> inviteUser(Authentication authentication, @PathVariable Long challengeId, @PathVariable Long userId){
        InviteCreateResponse inviteCreateResponse =participationService.inviteUser(authentication.getName(), challengeId, userId);
        return ResponseEntity.ok().body(inviteCreateResponse);
    }

/*    @GetMapping("/{challengeId}/certification")
    public ResponseEntity<List<ParticipantCertificationResponse>> findAllParticipantCertifications(Authentication authentication, @PathVariable Long challengeId){

    }*/
}
