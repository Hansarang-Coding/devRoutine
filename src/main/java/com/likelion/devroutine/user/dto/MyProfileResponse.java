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
    private String nickName;
    private String profileImageUrl;
    private Long followerCount;
    private Long followingCount;
    private List<ChallengeResponse> challenges; //참가중인 챌린지
    private List<ChallengeResponse> authorizedChallenges; //개설한 챌린지
    private User myUser;

    private List<User> byFollowingList;
    private List<User> byFollowerList;

    public static MyProfileResponse of(User user, Long followerCount, Long followingCount, List<Participation> participations
    ,List<User> byFollowingList, List<User> byFollowerList, User myUser) {
        List<ChallengeResponse> challenges = extractchallenges(participations);
        List<ChallengeResponse> authorizedChallenges = extractchallenges(participations, user.getId());
        return MyProfileResponse.builder()
                .nickName(user.getName())
                .profileImageUrl(user.getPicture())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .challenges(challenges)
                .authorizedChallenges(authorizedChallenges)
                .byFollowingList(byFollowingList)
                .byFollowerList(byFollowerList)
                .myUser(myUser)
                .build();
    }

    private static List<ChallengeResponse> extractchallenges(List<Participation> participations, Long userId) {
        return participations.stream()
                .filter(participation -> participation.getChallenge().getUserId().equals(userId))
                .map(participation -> ChallengeResponse.builder()
                        .id(participation.getChallenge().getId())
                        .message(participation.getChallenge().getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<ChallengeResponse> extractchallenges(List<Participation> participations) {
        return participations.stream()
                .map(participation -> ChallengeResponse.builder()
                        .id(participation.getChallenge().getId())
                        .message(participation.getChallenge().getTitle())
                        .build())
                .collect(Collectors.toList());
    }
}
