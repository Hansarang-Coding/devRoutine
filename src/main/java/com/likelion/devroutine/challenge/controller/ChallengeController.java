package com.likelion.devroutine.challenge.controller;

import com.likelion.devroutine.auth.config.LoginUser;
import com.likelion.devroutine.auth.dto.SessionUser;
import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.dto.ChallengeCreateResponse;
import com.likelion.devroutine.challenge.dto.ChallengeDto;
import com.likelion.devroutine.challenge.dto.ChallengeResponse;
import com.likelion.devroutine.challenge.service.ChallengeService;
import com.likelion.devroutine.participant.dto.ParticipationChallengeDto;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.enumerate.ResponseMessage;
import com.likelion.devroutine.participant.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;
    private final ParticipationService participationService;

    @GetMapping
    public String getChallenges(@RequestParam(required = false) String keyword, Model model,
                                Authentication authentication){
        List<ChallengeDto> challengeDtos;
        if(keyword==null) {
            challengeDtos = challengeService.findAllChallenge();
        }else{
            challengeDtos=challengeService.findAllChallengeTitle(keyword);
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("challenges", challengeDtos);
        model.addAttribute("hashtags", challengeService.getRandomHashTag());
        return "challenges/list";
    }

    @GetMapping("/new")
    public String createChallenge(Authentication authentication, Model model, ChallengeCreateRequest requestDto){
        model.addAttribute("requestDto", requestDto);
        return "challenges/new";
    }

    @PostMapping
    public String challenges(Authentication authentication, @ModelAttribute("requestDto") ChallengeCreateRequest challengeCreateRequest){
        ChallengeCreateResponse challengeCreateResponse=challengeService.createChallenge(authentication.getName(), challengeCreateRequest);
        log.info(challengeCreateRequest.getAuthenticationType().toString());
        log.info(challengeCreateRequest.getHashTag());
        log.info(challengeCreateRequest.getStartDate().toString());
        return "redirect:/challenges"+challengeCreateResponse.getChallengeId();
    }

    @GetMapping("/{challengeId}")
    public String showChallenge(@PathVariable Long challengeId, Model model,Authentication authentication, @LoginUser SessionUser sessionUser){
        if(sessionUser==null || sessionUser.getName()==null || !challengeService.isParticipate(challengeId, authentication.getName())){
            ChallengeDto challengeDto;
            if(sessionUser==null || sessionUser.getName()==null) challengeDto=challengeService.findByChallengeId(challengeId);
            else challengeDto=challengeService.findByChallengeId(challengeId, authentication.getName());
            model.addAttribute("challenge", challengeDto);
            return "challenges/detail";
        }
        ParticipationChallengeDto participationChallengeDto = participationService.findByParticipateChallenge(authentication.getName(), challengeId);
        model.addAttribute("challenge", participationChallengeDto);
        return "participations/detail";
    }

    @PostMapping("/{challengeId}")
    public String participateChallenge(@PathVariable Long challengeId, Authentication authentication){
        log.info("챌린지 참여 컨트롤러 시작");
        ParticipationResponse participationResponse=challengeService.participateChallenge(authentication.getName(), challengeId);
        return "redirect:/";
    }
}
