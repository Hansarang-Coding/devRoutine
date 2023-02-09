package com.likelion.devroutine.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.devroutine.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentResponse {
    private Long id;
    private String comment;
    private String userName;
    private String oauthId;
    private LocalDateTime createdAt;

    public static List<CommentResponse> of(Page<Comment> comments) {
        return comments.stream().
                map(comment -> CommentResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getName())
                .oauthId(comment.getUser().getOauthId())
                .createdAt(comment.getCreatedAt())
                .build())
                .collect(Collectors.toList());
    }
}
