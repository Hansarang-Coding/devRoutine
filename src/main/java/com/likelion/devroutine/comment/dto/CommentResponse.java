package com.likelion.devroutine.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.devroutine.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long certificationId;


    private LocalDateTime createdAt;

    public static Page<CommentResponse> of(Page<Comment> comments) {
        return comments.map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getName())
                //.certificationId(comment.getCertification().getId())
                .createdAt(comment.getCreatedAt())
                .build()
        );
    }
}
