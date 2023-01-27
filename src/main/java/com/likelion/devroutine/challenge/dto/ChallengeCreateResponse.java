package com.likelion.devroutine.challenge.dto;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ChallengeCreateResponse {
    private Long challengeId;
    private String title;
    private String description;
    private String vigibility;
    private AuthenticationType authenticationType;
    private List<String> keywordList;

    public static ChallengeCreateResponse toResponse(Challenge challenge, List<String> keywordList) {
        return ChallengeCreateResponse.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .description(challenge.getDescription())
                .vigibility(challenge.getVigibility()?"공개":"비공개")
                .authenticationType(challenge.getAuthenticationType())
                .keywordList(keywordList)
                .build();
    }
}
