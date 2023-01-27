package com.likelion.devroutine.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.devroutine.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateResponse {

    private Long id;
    private String comment;
    private String userName;
    private String certificationId;


    private LocalDateTime createdAt;

    public static CommentCreateResponse of(Comment savedComment) {
        return CommentCreateResponse.builder()
                .id(savedComment.getId())
                .comment(savedComment.getComment())
                .userName(savedComment.getUser().getName())
                //.certificationId(savedComment.getCertification().getId;
                .createdAt(savedComment.getCreatedAt())
                .build();
    }
}
