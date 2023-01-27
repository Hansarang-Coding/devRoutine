package com.likelion.devroutine.comment.controller;

import com.likelion.devroutine.comment.dto.CommentCreateResponse;
import com.likelion.devroutine.comment.dto.CommentRequest;
import com.likelion.devroutine.comment.dto.CommentResponse;
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

        String authentication = "user";

        CommentCreateResponse response = commentService.createComment(certificationId, request, authentication);
        //로그인 확인해야함 authentication
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{certificationId}/comments")
    public ResponseEntity<Page<CommentResponse>> findAllComments(
            @PathVariable("certificationId") Long certificationId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(commentService.findAll(certificationId,pageable));

    }




}
