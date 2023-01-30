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
public class FollowingResponse {

    private String name;
    private String picture;

    public static List<FollowingResponse> of(List<Follow> followings){
        return followings.stream()
                .map(follow -> FollowingResponse.builder()
                        .name(follow.getFollower().getName())
                        .picture(follow.getFollower().getPicture())
                        .build())
                .collect(Collectors.toList());
    }
}
