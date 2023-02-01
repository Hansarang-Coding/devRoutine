package com.likelion.devroutine.follow.dto;

import com.likelion.devroutine.follow.domain.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class FollowerResponse {
    private Long id;
    private String name;
    private String picture;

    public static List<FollowerResponse> of(List<Follow> followers){
        return followers.stream()
                .map(follow -> FollowerResponse.builder()
                        .id(follow.getFollowing().getId())
                        .name(follow.getFollowing().getName())
                        .picture(follow.getFollowing().getPicture())
                        .build())
                .collect(Collectors.toList());
    }
}
