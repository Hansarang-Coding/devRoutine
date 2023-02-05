package com.likelion.devroutine.comment.dto;

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
    private LocalDateTime createdAt;

    public static CommentCreateResponse of(Comment savedComment) {
        return CommentCreateResponse.builder()
                .id(savedComment.getId())
                .comment(savedComment.getComment())
                .userName(savedComment.getUser().getName())
                .createdAt(savedComment.getCreatedAt())
                .build();
    }
}
