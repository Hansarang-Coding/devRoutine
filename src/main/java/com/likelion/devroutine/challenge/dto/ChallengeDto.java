package com.likelion.devroutine.challenge.dto;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ChallengeDto {
    private Long id;
    private String title;
    private String description;
    private AuthenticationType authenticationType;
    private String vigibility;
    private Long fromUserId;
    private List<ChallengeHashTagResponse> challengeHashTag;

    public static List<ChallengeDto> toList(List<Challenge> challenges, Map<Long, List<ChallengeHashTagResponse>> challengeHashTags) {
        return challenges.stream()
                .map(challenge -> ChallengeDto.builder()
                        .id(challenge.getId())
                        .title(challenge.getTitle())
                        .description(challenge.getDescription())
                        .authenticationType(challenge.getAuthenticationType())
                        .fromUserId(challenge.getUserId())
                        .challengeHashTag(challengeHashTags.get(challenge.getId()))
                        .vigibility(challenge.getVigibility() ? "공개" : "비공개")
                        .build())
                .collect(Collectors.toList());
    }

    public static ChallengeDto toDto(Challenge challenge, List<ChallengeHashTagResponse> challengeHashTag) {
        return ChallengeDto.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .authenticationType(challenge.getAuthenticationType())
                .fromUserId(challenge.getUserId())
                .challengeHashTag(challengeHashTag)
                .vigibility(challenge.getVigibility() ? "공개" : "비공개")
                .build();
    }
}
