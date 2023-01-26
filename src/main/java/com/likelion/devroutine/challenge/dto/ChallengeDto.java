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
public class ChallengeDto {
    private Long id;
    private String title;
    private String description;
    private AuthenticationType authenticationType;
    private String vigibility;
    private Long fromUserId;
    private List<String> hashTag;

    public static List<ChallengeDto> toList(List<Challenge> challenges) {
        return challenges.stream()
                .filter(entity->entity.getVigibility())
                .map(entity -> ChallengeDto.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .description(entity.getDescription())
                        .authenticationType(entity.getAuthenticationType())
                        .fromUserId(entity.getUser().getId())
                        .hashTag(List.of("알고리즘", "사진인증"))
                        .vigibility(entity.getVigibility() ? "공개" : "비공개")
                        .build())
                .collect(Collectors.toList());
    }

    public static ChallengeDto toDto(Challenge challenge) {
        return ChallengeDto.builder()
                .id(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .authenticationType(challenge.getAuthenticationType())
                .fromUserId(challenge.getUser().getId())
                .hashTag(List.of("알고리즘", "사진인증"))
                .vigibility(challenge.getVigibility() ? "공개" : "비공개")
                .build();
    }
}
