package com.likelion.devroutine.participant.controller;

import com.likelion.devroutine.participant.dto.ParticipationResponse;
import com.likelion.devroutine.participant.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/challenges")
@Slf4j
@RequiredArgsConstructor
public class ParticipationController {
    private final ParticipationService participationService;

    @GetMapping("/{challengeId}/cancel")
    public String cancelChallenge(@PathVariable Long challengeId, Authentication authentication){
        try {
            ParticipationResponse participationResponse = participationService.cancelChallenge(authentication.getName(), challengeId);
            return "redirect:/challenges/" + String.valueOf(challengeId);
        }catch(Exception e){
            return "error/error";
        }
    }
}
