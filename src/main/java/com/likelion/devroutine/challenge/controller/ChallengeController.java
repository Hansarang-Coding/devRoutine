package com.likelion.devroutine.challenge.controller;

import com.likelion.devroutine.auth.config.LoginUser;
import com.likelion.devroutine.auth.dto.SessionUser;
import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.service.ChallengeService;
import com.likelion.devroutine.participant.dto.ParticipationChallengeDto;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.enumerate.ResponseMessage;
import com.likelion.devroutine.participant.service.ParticipationService;
import com.likelion.devroutine.user.dto.UserResponse;
import com.likelion.devroutine.user.exception.UserNotFoundException;
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
    public String createChallenge(Authentication authentication){
        return "challenges/new";
    }

    @PostMapping("/new")
    public String saveChallenges(Authentication authentication, ChallengeCreateRequest requestDto){
        try {
            ChallengeCreateResponse challengeCreateResponse = challengeService.createChallenge(authentication.getName(), requestDto);
            return "redirect:/challenges/"+challengeCreateResponse.getChallengeId();
        }catch(UserNotFoundException e) {
            return "error/error";
        }
    }

    @GetMapping("/{challengeId}")
    public String showChallenge(@PathVariable Long challengeId, Model model,Authentication authentication, @LoginUser SessionUser sessionUser){
        if(sessionUser==null || sessionUser.getName()==null || !challengeService.isParticipate(challengeId, authentication.getName())){
            ChallengeDto challengeDto;
            if(sessionUser==null || sessionUser.getName()==null) challengeDto=challengeService.findByChallengeId(challengeId);
            else challengeDto=challengeService.findByChallengeId(challengeId, authentication.getName());
            model.addAttribute("challenge", challengeDto);
            log.info("참여중이지 않은 상세조회");
            return "challenges/detail";
        }
        model.addAttribute("user", challengeService.getUserResponse(authentication.getName()));
        model.addAttribute("challenge", challengeService.findByChallengeId(challengeId, authentication.getName()));
        model.addAttribute("participationChallenge", participationService.findByParticipateChallenge(authentication.getName(), challengeId));
        model.addAttribute("followers", participationService.findFollowers(authentication.getName(), challengeId));
        return "participations/detail";
    }

    @PostMapping("/{challengeId}")
    public String participateChallenge(@PathVariable Long challengeId, Authentication authentication){
        log.info("챌린지 참여 컨트롤러 시작");
        ParticipationResponse participationResponse=challengeService.participateChallenge(authentication.getName(), challengeId);
        return "redirect:/challenges/{challengeId}";
    }

    @GetMapping("/{challengeId}/edit")
    public String editChallengeForm(@PathVariable Long challengeId, Authentication authentication, Model model){
        ChallengeDto challengeDto=challengeService.findByChallengeId(challengeId, authentication.getName());
        model.addAttribute("challenge", challengeDto);
        model.addAttribute("hashtag", challengeService.getHashTagString(challengeDto.getChallengeHashTag()));
        return "challenges/edit";
    }

    @PostMapping("/{challengeId}/edit")
    public String updateChallenge(@PathVariable Long challengeId, Authentication authentication, Model model, ChallengeModifiyRequest challengeModifiyRequest){
        log.info("title : "+challengeModifiyRequest.getTitle());
        log.info("hashtag : "+challengeModifiyRequest.getHashtag());
        ChallengeResponse challengeResponse=challengeService.modifyChallenge(authentication.getName(), challengeId, challengeModifiyRequest);
        return "redirect:/challenges/"+String.valueOf(challengeId);
    }

    @GetMapping("/{challengeId}/delete")
    public String deleteChallenge(@PathVariable Long challengeId, Authentication authentication){
        ChallengeResponse challengeResponse=challengeService.deleteChallenge(authentication.getName(), challengeId);
        return "redirect:/challenges";
    }
}
