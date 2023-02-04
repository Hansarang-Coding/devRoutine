package com.likelion.devroutine.certification.controller;

import com.likelion.devroutine.certification.dto.CertificationCreateRequest;
import com.likelion.devroutine.certification.dto.CertificationListResponse;
import com.likelion.devroutine.certification.dto.amazons3.FileUploadResponse;
import com.likelion.devroutine.certification.service.AmazonS3UploadService;
import com.likelion.devroutine.certification.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CertificationController {

    private final CertificationService certificationService;
    private final AmazonS3UploadService s3UploadService;

    @GetMapping("/certification/{participationId}")
    public String getCertificationForm(@PathVariable("participationId") Long participationId,
                                       Model model, Authentication authentication) {
        model.addAttribute("participation", certificationService
                .findParticipationFormInfo(participationId, authentication.getName()));
        return "certification/form";
    }

    @GetMapping("/certification")
    public String getCertificationList(Authentication authentication, Model model) {
        List<CertificationListResponse> certifications = certificationService
                .findAllParticipationByUser(authentication.getName());
        model.addAttribute("certifications", certifications);
        return "certification/list";
    }

    @PostMapping("/certification/{participationId}")
    public String createCertification(@PathVariable Long participationId,
                                      CertificationCreateRequest request,
                                      Authentication authentication) {
        try {
            FileUploadResponse certification = s3UploadService
                    .uploadFiles(participationId, request.getCertImage(), "certificationFile");
            certificationService.createCertification(participationId, request, authentication.getName(), certification.getUploadImageUrl());
            return "redirect:/certification";
        } catch (IOException e) {
            return "error";
        }
    }
}
