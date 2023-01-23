package com.likelion.devroutine.challenge.dto;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ChallengeResponse {
    private Long id;
    private String title;
    private String description;
    private AuthenticationType authenticationType;
    private String vigibility;
    private Long fromUserId;
    private List<String> hashTag;

    public static List<ChallengeResponse> toList(List<Challenge> challenges) {
        return challenges.stream()
                .filter(entity->entity.isVigibility())
                .map(entity -> ChallengeResponse.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .description(entity.getDescription())
                        .authenticationType(entity.getAuthenticationType())
                        .fromUserId(entity.getFromUserId())
                        .hashTag(List.of("알고리즘", "사진인증"))
                        .vigibility(entity.isVigibility() ? "공개" : "비공개")
                        .build())
                .collect(Collectors.toList());
    }

    public static ChallengeResponse toResponse(Challenge challenge) {
        return ChallengeResponse.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .authenticationType(challenge.getAuthenticationType())
                .fromUserId(challenge.getFromUserId())
                .hashTag(List.of("알고리즘", "사진인증"))
                .vigibility(challenge.isVigibility() ? "공개" : "비공개")
                .build();
    }
}
