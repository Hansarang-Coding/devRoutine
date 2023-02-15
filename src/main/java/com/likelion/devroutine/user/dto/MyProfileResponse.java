package com.likelion.devroutine.user.dto;

import com.likelion.devroutine.challenge.dto.ChallengeResponse;
import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class MyProfileResponse {
    private Long userId;
    private String nickName;
    private String oauthId;
    private String profileImageUrl;
    private Long followerCount;
    private Long followingCount;

    private List<User> byFollowingList;
    private List<User> byFollowerList;

    public static MyProfileResponse of(User user, Long followerCount, Long followingCount, List<User> byFollowingList, List<User> byFollowerList) {
        return MyProfileResponse.builder()
                .userId(user.getId())
                .nickName(user.getName())
                .oauthId(user.getOauthId())
                .profileImageUrl(user.getPicture())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .byFollowingList(byFollowingList)
                .byFollowerList(byFollowerList)
                .build();
    }
}
