package com.likelion.devroutine.challenge.controller;

import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.service.ChallengeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/challenges")
public class ChallengeRestController {
    private final ChallengeService challengeService;

    public ChallengeRestController(ChallengeService challengeService) {
        this.challengeService = challengeService;
    }
    @GetMapping
    public ResponseEntity<List<ChallengeDto>> findAllChallengeList(Long challengeId,
                                                                   @RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String keyword){
        List<ChallengeDto> challengeDtos;
        if(keyword==null) {
            challengeDtos = challengeService.findAllChallenge(challengeId, size);
        }else{
            challengeDtos=challengeService.findAllChallengeTitle(challengeId, size, keyword);
        }
        return ResponseEntity.ok().body(challengeDtos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDto> findByChallengeId(@PathVariable Long id){
        // 로그인 되어있는지 확인
        // 로그인된 유저가 참여중인 챌린지이면 /{chanllengeId}/challenges-detail/{id}로 리다이렉트

        //로그인 X 이거나 참여중이지 않은 챌린지 인경우
        ChallengeDto challengeDto=challengeService.findByChallengeId(id);
        return ResponseEntity.ok().body(challengeDto);
    }
    @PostMapping
    public ResponseEntity<ChallengeCreateResponse> createChallenge(@RequestBody ChallengeCreateRequest dto){
        //로그인 되어있는 사용자만 가능
        ChallengeCreateResponse challengeCreateResponse=challengeService.createChallenge(dto);
        return ResponseEntity.ok().body(challengeCreateResponse);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ChallengeResponse> deleteChallenge(@PathVariable Long id){
        //로그인한 사용자만 가능
        ChallengeResponse challengeResponse=challengeService.deleteChallenge(id);
        return ResponseEntity.ok().body(challengeResponse);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ChallengeResponse> modifyChallenge(@PathVariable Long id, @RequestBody ChallengeModifiyRequest dto){
        //로그인한 사용자만 가능
        ChallengeResponse challengeResponse=challengeService.modifyChallenge(id, dto);
        return ResponseEntity.ok().body(challengeResponse);
    }
}
