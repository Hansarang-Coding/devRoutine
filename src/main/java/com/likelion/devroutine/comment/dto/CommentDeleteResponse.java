package com.likelion.devroutine.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CommentDeleteResponse {

    private Long id;
    private String message;

    public static CommentDeleteResponse of(String message, Long commentId) {
        return CommentDeleteResponse.builder()
                .message(message)
                .id(commentId)
                .build();
    }
}
