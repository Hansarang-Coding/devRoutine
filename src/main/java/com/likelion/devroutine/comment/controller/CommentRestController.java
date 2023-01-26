package com.likelion.devroutine.comment.controller;

import com.likelion.devroutine.comment.dto.CommentCreateResponse;
import com.likelion.devroutine.comment.dto.CommentRequest;
import com.likelion.devroutine.comment.service.CommentService;
import com.nimbusds.oauth2.sdk.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/certification")
@RestController
public class CommentRestController {

    private final CommentService commentService;
    //댓글 생성
    @PostMapping("/{certificationId}/comments")
    public ResponseEntity<CommentCreateResponse> createComment(
            @PathVariable("certificationId") Long certificationId,
            @Valid @RequestBody CommentRequest request) {
        //로그인 확인해야함 authentication
        log.info(request.toString());
        CommentCreateResponse response = commentService.createComment(7L,request,7L);
        //로그인 확인해야함 authentication
        return ResponseEntity.ok().body(response);
    }
}
