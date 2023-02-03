package com.likelion.devroutine.certification.controller;

import com.likelion.devroutine.certification.dto.CertificationCreateRequest;
import com.likelion.devroutine.certification.dto.CertificationCreateResponse;
import com.likelion.devroutine.certification.dto.CertificationResponse;
import com.likelion.devroutine.certification.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CertificationRestController {

    private final CertificationService certificationService;

    @PostMapping("/certification/{challengeId}")
    public ResponseEntity<CertificationCreateResponse> createCertification(@PathVariable Long challengeId,
                                                                           CertificationCreateRequest request,
                                                                           Authentication authentication) {
        CertificationCreateResponse response = certificationService
                .createCertification(challengeId, request, authentication.getName());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/certification/{challengeId}")
    public ResponseEntity<List<CertificationResponse>> findCertification(@PathVariable Long challengeId,
                                                                         Authentication authentication) {
        List<CertificationResponse> response = certificationService
                .findCertifications(challengeId, authentication.getName());
        return ResponseEntity.ok().body(response);
    }
}
