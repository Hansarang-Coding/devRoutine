package com.likelion.devroutine.challenge.controller;

import com.likelion.devroutine.auth.config.LoginUser;
import com.likelion.devroutine.auth.dto.SessionUser;
import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.challenge.service.ChallengeService;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.service.ParticipationService;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        try {
            List<ChallengeDto> challengeDtos;
            if (keyword == null) {
                challengeDtos = challengeService.findAllChallenge();
            } else {
                challengeDtos = challengeService.findAllChallengeTitle(keyword);
            }
            model.addAttribute("keyword", keyword);
            model.addAttribute("challenges", challengeDtos);
            model.addAttribute("hashtags", challengeService.getRandomHashTag());
            return "challenges/list";
        }catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error/challengeError";
        }
    }

    @GetMapping("/new")
    public String createChallenge(Authentication authentication){
        return "challenges/new";
    }

    @PostMapping("/new")
    public String saveChallenges(Authentication authentication, ChallengeCreateRequest requestDto, Model model){
        try {
            ChallengeCreateResponse challengeCreateResponse = challengeService.createChallenge(authentication.getName(), requestDto);
            return "redirect:/challenges/"+challengeCreateResponse.getChallengeId();
        }catch(UserNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/challengeError";
        }
    }

    @GetMapping("/{challengeId}")
    public String showChallenge(@PathVariable Long challengeId, Model model,Authentication authentication, @LoginUser SessionUser sessionUser){
        try {
            if (sessionUser == null || sessionUser.getName() == null || !challengeService.isParticipate(challengeId, authentication.getName())) {
                ChallengeDto challengeDto;
                if (sessionUser == null || sessionUser.getName() == null)
                    challengeDto = challengeService.findByChallengeId(challengeId);
                else challengeDto = challengeService.findByChallengeId(challengeId, authentication.getName());
                model.addAttribute("challenge", challengeDto);
                log.info("참여중이지 않은 상세조회");
                return "challenges/detail";
            }
            ChallengeDto challenge=challengeService.findByChallengeId(challengeId, authentication.getName());
            model.addAttribute("user", challengeService.getUserResponse(authentication.getName()));
            model.addAttribute("challenge", challenge);
            model.addAttribute("participationChallenge", participationService.findByParticipateChallenge(authentication.getName(), challengeId));
            model.addAttribute("followers", participationService.findFollowers(authentication.getName(), challengeId));
            if(challenge.getAuthenticationType().equals(AuthenticationType.GITHUB)){
                return "participations/githubDetail";
            }
            return "participations/imageDetail";
        }catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            log.info(e.getMessage());
            return "error/challengeError";
        }
    }

    @PostMapping("/{challengeId}")
    public String participateChallenge(@PathVariable Long challengeId, Authentication authentication, Model model){
        try {
            ParticipationResponse participationResponse = challengeService.participateChallenge(authentication.getName(), challengeId);
            return "redirect:/challenges/{challengeId}";
        }catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error/challengeError";
        }
    }

    @GetMapping("/{challengeId}/edit")
    public String editChallengeForm(@PathVariable Long challengeId, Authentication authentication, Model model){
        try {
            ChallengeDto challengeDto = challengeService.findByChallengeId(challengeId, authentication.getName());
            model.addAttribute("challenge", challengeDto);
            model.addAttribute("hashtag", challengeService.getHashTagString(challengeDto.getChallengeHashTag()));
            return "challenges/edit";
        }catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error/challengeError";
        }
    }

    @PostMapping("/{challengeId}/edit")
    public String updateChallenge(@PathVariable Long challengeId, Authentication authentication, Model model, ChallengeModifiyRequest challengeModifiyRequest){
        try{
            ChallengeResponse challengeResponse=challengeService.modifyChallenge(authentication.getName(), challengeId, challengeModifiyRequest);
            return "redirect:/challenges/"+String.valueOf(challengeId);
        }catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error/challengeError";
        }
    }

    @GetMapping("/{challengeId}/delete")
    public String deleteChallenge(@PathVariable Long challengeId, Authentication authentication, Model model){
        try {
            ChallengeResponse challengeResponse = challengeService.deleteChallenge(authentication.getName(), challengeId);
            return "redirect:/challenges";
        }catch(Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "error/challengeError";
        }
    }
}