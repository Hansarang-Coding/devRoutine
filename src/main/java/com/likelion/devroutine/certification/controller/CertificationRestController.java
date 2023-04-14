package com.likelion.devroutine.certification.controller;

import com.likelion.devroutine.certification.dto.github.CommitApiDto;
import com.likelion.devroutine.certification.dto.github.RepositoryDto;
import com.likelion.devroutine.certification.service.CertificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certification")
@RequiredArgsConstructor
@Slf4j
public class CertificationRestController {
    private final CertificationService certificationService;

    @GetMapping("/{participationId}/repos")
    public ResponseEntity<List<RepositoryDto>> getUserRepository(@PathVariable Long participationId, Authentication authentication){
        log.info("rest controller start");
        return ResponseEntity.ok()
                .body(certificationService.getUserRepository(participationId, authentication.getName()));
    }

    @GetMapping("/{participationId}/repos/{repoName}/commits")
    public ResponseEntity<List<CommitApiDto>> getRepositoryCommits(@PathVariable Long participationId, @PathVariable String repoName,
                                                                   Authentication authentication){
        return ResponseEntity.ok()
                .body(certificationService.getRepositoryCommits(repoName, participationId, authentication.getName()));
    }
}

