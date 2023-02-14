package com.likelion.devroutine.hashtag.dto;

import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.hashtag.domain.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
public class HashTagResponse {
    private String hashTag;

    public static List<HashTagResponse> of(List<HashTag> hashTags){
        return hashTags.stream()
                .map(hashTag -> HashTagResponse.builder()
                        .hashTag(hashTag.getContents())
                        .build())
                .collect(Collectors.toList());
    }
}
