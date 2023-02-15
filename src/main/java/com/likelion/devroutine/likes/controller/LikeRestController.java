package com.likelion.devroutine.likes.controller;

import com.likelion.devroutine.likes.dto.LikeResponse;
import com.likelion.devroutine.likes.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/certification")
@RequiredArgsConstructor
@RestController
public class LikeRestController {

    private final LikeService likeService;

    @PostMapping("/{certificationId}/likes")
    public ResponseEntity<LikeResponse> doLike(@PathVariable Long certificationId, Authentication authentication) {
        LikeResponse likeResponse = likeService.doLikes(certificationId, authentication.getName());
        return ResponseEntity.ok().body(likeResponse);
    }

    @GetMapping("/{certificationId}/likes")
    public Integer countLikes(@PathVariable Long certificationId) {
        return likeService.countLikes(certificationId);
    }

    @DeleteMapping("/{certificationId}/likes")
    public void deleteLike(@PathVariable Long certificationId, Authentication authentication) {
        String oauthId = authentication.getName();
        likeService.deleteLikes(certificationId, oauthId);
    }
}
