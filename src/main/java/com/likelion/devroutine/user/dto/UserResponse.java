package com.likelion.devroutine.user.dto;

import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String picture;
    private UserRole role;
    private String oauthId;

    public static List<UserResponse> toList(List<User> users){
        return users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .picture(user.getPicture())
                        .role(user.getRole())
                        .oauthId(user.getOauthId())
                        .build())
                .collect(Collectors.toList());
    }

    public static UserResponse of(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .picture(user.getPicture())
                .role(user.getRole())
                .oauthId(user.getOauthId())
                .build();
    }
}
