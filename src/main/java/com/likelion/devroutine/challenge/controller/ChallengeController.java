package com.likelion.devroutine.challenge.controller;

import com.likelion.devroutine.challenge.dto.ChallengeResponse;
import com.likelion.devroutine.challenge.service.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/challenges")
public class ChallengeController {
    private final ChallengeService challengeService;

    public ChallengeController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }
    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> findAllChallengeList(@RequestParam(defaultValue = "0") Long challengeId,
                                                                        @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String keyword){
        List<ChallengeResponse> challengeResponses;
        if(keyword==null) {
            challengeResponses = challengeService.findAllChallenge(challengeId, size);
        }else{
            challengeResponses=challengeService.findAllChallengeTitle(challengeId, size, keyword);
        }
        return ResponseEntity.ok().body(challengeResponses);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponse> findByChallengeId(@PathVariable Long id){
        // 로그인 되어있는지 확인
        // 로그인된 유저가 참여중인 챌린지이면 /{chanllengeId}/challenges-detail/{id}로 리다이렉트

        //로그인 X 이거나 참여중이지 않은 챌린지 인경우
        ChallengeResponse challengeResponse=challengeService.findByChallengeId(id);
        return ResponseEntity.ok().body(challengeResponse);
    }
}
