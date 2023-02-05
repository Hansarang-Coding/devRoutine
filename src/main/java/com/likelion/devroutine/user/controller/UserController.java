package com.likelion.devroutine.user.controller;

import com.likelion.devroutine.user.dto.MyProfileResponse;
import com.likelion.devroutine.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

//    @GetMapping("/users/{userId}")
//    public String getProfile(Authentication authentication,@PathVariable Long userId,
//                             Model model) {
//        MyProfileResponse profile = userService.getProfile(authentication.getName(), userId);
//        model.addAttribute("profile", profile);
//        return "user/profile";
//    }
}
