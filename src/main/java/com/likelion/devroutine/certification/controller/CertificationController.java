package com.likelion.devroutine.certification.controller;

import com.likelion.devroutine.certification.dto.CertificationCreateRequest;
import com.likelion.devroutine.certification.dto.CertificationFormResponse;
import com.likelion.devroutine.certification.dto.CertificationResponse;
import com.likelion.devroutine.certification.dto.ParticipationResponse;
import com.likelion.devroutine.certification.dto.amazons3.FileUploadResponse;
import com.likelion.devroutine.certification.service.AmazonS3UploadService;
import com.likelion.devroutine.certification.service.CertificationService;
import com.likelion.devroutine.comment.service.CommentService;
import com.likelion.devroutine.likes.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping("/certification/post/{participationId}")
    public String getCertificationForm(@PathVariable("participationId") Long participationId,
                                       Model model, Authentication authentication) {
        try {
            CertificationFormResponse certificationFormInfo = certificationService
                    .findCertificationFormInfo(participationId, authentication.getName());
            model.addAttribute("participation", certificationFormInfo);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error/error";
        }
        return "certification/form";
    }

    @GetMapping("/certification")
    public String getCertificationList(Authentication authentication, Model model) {
        List<ParticipationResponse> participations = certificationService
                .findAllParticipationByUser(authentication.getName());
        List<CertificationResponse> certifications = certificationService.findCertifications();
        model.addAttribute("participations", participations);
        model.addAttribute("certifications", certifications);
        return "certification/list";
    }

    @PostMapping("/certification/post/{participationId}")
    public String createCertification(@PathVariable Long participationId,
                                      CertificationCreateRequest request,
                                      Authentication authentication) {
        FileUploadResponse response = s3UploadService
                .upload(request.getCertImage(), "certificationFile");
        certificationService.createCertification(participationId, request, authentication.getName(), response.getUploadImageUrl());
        return "redirect:/certification";
    }

    @GetMapping("/certification/{certificationId}")
    public String getCertificationDetail(@PathVariable Long certificationId, Model model,
                                         Authentication authentication,
                                         @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("certification", certificationService
                .findCertificationDetail(certificationId));
        model.addAttribute("comments", commentService.findAll(certificationId, pageable));
        model.addAttribute("isPressed", likeService.isAlreadyPressedLike(certificationId,
                authentication.getName()));
        model.addAttribute("likeCount", likeService.countLikes(certificationId));
        return "certification/detail";
    }
}
