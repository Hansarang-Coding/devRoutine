package com.likelion.devroutine.hashtag.dto;

import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChallengeHashTagResponse {

    private String hashTag;

    public static List<ChallengeHashTagResponse> of(List<ChallengeHashTag> challengeHashTags){
        return challengeHashTags.stream()
                .map(challengeHashTag -> ChallengeHashTagResponse.builder()
                        .hashTag(challengeHashTag.getHashTag().getContents())
                        .build())
                .collect(Collectors.toList());
    }
}
