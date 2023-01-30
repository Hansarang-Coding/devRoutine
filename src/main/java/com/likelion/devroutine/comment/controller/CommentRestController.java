package com.likelion.devroutine.comment.controller;

import com.likelion.devroutine.comment.dto.*;
import com.likelion.devroutine.comment.service.CommentService;
import com.nimbusds.oauth2.sdk.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        //로그인 확인해야함 authentication

        CommentCreateResponse response = commentService.createComment(certificationId, request, authentication.getName());
        //로그인 확인해야함 authentication
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{certificationId}/comments")
    public ResponseEntity<Page<CommentResponse>> findAllComments(
            @PathVariable("certificationId") Long certificationId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().body(commentService.findAll(certificationId,pageable));

    }

    @DeleteMapping("/{certificationId}/comments/{id}")
    public ResponseEntity<CommentDeleteResponse> deleteComment(
            @PathVariable("certificationId") Long certificationId,
            @PathVariable("id") Long commentId,
            Authentication authentication){
        return ResponseEntity.ok().body(commentService.deleteComment(certificationId, commentId, authentication.getName()));
    }

    @PutMapping("/{certificationId}/comments/{id}")
    public ResponseEntity<CommentUpdateResponse> updateComment(
            @PathVariable("certificationId") Long certificationId,
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {
        return ResponseEntity.ok().body(commentService.updateComment(certificationId, commentId, request, authentication.getName()));
    }




}
