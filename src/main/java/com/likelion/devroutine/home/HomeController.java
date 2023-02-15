package com.likelion.devroutine.home;

import com.likelion.devroutine.auth.config.LoginUser;
import com.likelion.devroutine.auth.dto.SessionUser;
import com.likelion.devroutine.participant.service.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final ParticipationService participationService;

    @GetMapping("/")
    public String home(Model model, @LoginUser SessionUser user) {
        if (user != null) {
            model.addAttribute("userName", user.getName());
        }
        model.addAttribute("participation", participationService.findPopularParticipation());
        return "index";
    }
}
