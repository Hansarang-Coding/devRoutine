package com.likelion.devroutine.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FollowCreateResponse {
    private String message;

    public static FollowCreateResponse of(String followingUserName, String followerName) {
        return FollowCreateResponse.builder()
                .message(followingUserName + "님이" + followerName + "님을 팔로우 하기 시작 했습니다.")
                .build();
    }
}
