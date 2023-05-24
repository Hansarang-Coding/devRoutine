package com.likelion.devroutine.support;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.hashtag.domain.HashTag;

import java.util.List;

public class ChallengeHashTagFixture {
    public static ChallengeHashTag getChallengeHashTag(Challenge challenge, HashTag hashtag){
        return ChallengeHashTag.builder()
                .id(1L)
                .challenge(challenge)
                .hashTag(hashtag)
                .build();
    }
}
