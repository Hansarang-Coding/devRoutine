package com.likelion.devroutine.likes.dto;

import com.likelion.devroutine.likes.domain.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LikeResponse {
    private String message;
    private String oauthId;

    public static LikeResponse of(String message, Like like) {
        return LikeResponse.builder()
                .message(message)
                .oauthId(like.getUser().getOauthId())
                .build();
    }

    public static LikeResponse of(String message) {
        return LikeResponse.builder()
                .message(message)
                .build();
    }
}
