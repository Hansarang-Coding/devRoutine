package com.likelion.devroutine.certification.controller;

import com.likelion.devroutine.certification.dto.CertificationCreateRequest;
import com.likelion.devroutine.certification.dto.CertificationCreateResponse;
import com.likelion.devroutine.certification.dto.amazons3.FileUploadResponse;
import com.likelion.devroutine.certification.service.AmazonS3UploadService;
import com.likelion.devroutine.certification.service.CertificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class CertificationRestController {

    private final CertificationService certificationService;
    private final AmazonS3UploadService s3UploadService;

    @PostMapping("/certification/{participationId}")
    public ResponseEntity<CertificationCreateResponse> createCertification(@PathVariable Long participationId,
                                                                           CertificationCreateRequest request,
                                                                           Authentication authentication) {
        try {
            FileUploadResponse certification = s3UploadService
                    .uploadFiles(participationId, request.getCertImage(), "certificationFile");
            return ResponseEntity.ok().body(certificationService
                    .createCertification(participationId, request, authentication.getName(), certification.getUploadImageUrl()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
