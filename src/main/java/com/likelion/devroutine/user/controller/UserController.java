package com.likelion.devroutine.user.controller;

import com.likelion.devroutine.participant.service.ParticipationService;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.dto.MyProfileResponse;
import com.likelion.devroutine.user.dto.UserResponse;
import com.likelion.devroutine.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;
    private final ParticipationService participationService;

    @GetMapping("/profile")
    public String getProfile(Authentication authentication,
                             Model model) {
        MyProfileResponse profile = userService.getProfile(authentication.getName());
        model.addAttribute("profile", profile);
        model.addAttribute("participationChallenge", participationService.findAllParticipateChallenge(authentication.getName()));
        model.addAttribute("finishChallenge", participationService.findAllFinishChallenge(authentication.getName()));
        model.addAttribute("createdChallenge", participationService.findCreatedChallenge(authentication.getName()));
        return "user/profile";
    }

    @GetMapping("profile/{userId}")
    public String getOther(Authentication authentication,
                           @PathVariable Long userId,
                           Model model) {
        MyProfileResponse profile = userService.getOther(authentication.getName(), userId);
        if (profile.getOauthId().equals(authentication.getName())) {
            return "redirect:/profile";
        }
        model.addAttribute("profile", profile);
        model.addAttribute("participationChallenge", participationService.findAllParticipateChallenge(profile.getOauthId()));
        model.addAttribute("finishChallenge", participationService.findAllFinishChallenge(profile.getOauthId()));
        model.addAttribute("createdChallenge", participationService.findCreatedChallenge(profile.getOauthId()));
        model.addAttribute("followingStatus", userService.isFollowing(userId, authentication.getName()));
        return "user/other";
    }

    @GetMapping("profile/{userId}/challenges/{challengeId}")
    public String showChallenge(Authentication authentication, @PathVariable Long challengeId, @PathVariable Long userId, Model model) {
        try {
            participationService.isViewable(challengeId, authentication.getName());
            return "redirect:/challenges/" + String.valueOf(challengeId);
        }catch(Exception e){
            model.addAttribute("userId", userId);
            model.addAttribute("errorMessage", e.getMessage());
            return "error/profileError";
        }
    }
}