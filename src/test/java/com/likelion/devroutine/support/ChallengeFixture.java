package com.likelion.devroutine.support;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.user.domain.User;

import java.util.List;

public class ChallengeFixture {
    public static Challenge getChallenge(User user, List<ChallengeHashTag> challengeHashTags){
        return Challenge.builder()
                .id(1l)
                .title("1일 1알고리즘")
                .description("하루에 알고리즘 한문제 이상 풀기")
                .vigibility(true)
                .authenticationType(AuthenticationType.PICTURE)
                .challengeHashTags(challengeHashTags)
                .userId(user.getId())
                .build();
    }
}
