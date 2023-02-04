package com.likelion.devroutine.participant.controller;

import com.likelion.devroutine.challenge.dto.ChallengeDto;
import com.likelion.devroutine.participant.dto.ParticipationChallengeDto;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.service.ParticipationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/certification")
    public ResponseEntity<List<ChallengeDto>> findAllParticipateChallenge(Authentication authentication){
        List<ChallengeDto> challengeDtos=participationService.findAllParticipateChallenge(authentication.getName());
        return ResponseEntity.ok().body(challengeDtos);
    }
}
