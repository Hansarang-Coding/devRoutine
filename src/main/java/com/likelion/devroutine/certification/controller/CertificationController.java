package com.likelion.devroutine.certification.controller;

import com.likelion.devroutine.certification.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Controller
public class CertificationController {

    private final CertificationService certificationService;

    @GetMapping("/{challengeId}/certificate")
    public String certificateForm(@PathVariable("challengeId") Long challengeId,
                                  Model model, Authentication authentication) {
        model.addAttribute("challenge", certificationService
                .findParticipationInfo(challengeId, authentication.getName()));
        model.addAttribute("challengeId", challengeId);
        return "certification/form";
    }
}
