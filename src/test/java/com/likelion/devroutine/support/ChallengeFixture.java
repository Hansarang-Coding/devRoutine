package com.likelion.devroutine.support;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;

public class ChallengeFixture {
    public static Challenge getChallenge(User user){
        return Challenge.builder()
                .id(1l)
                .title("1일 1알고리즘")
                .description("하루에 알고리즘 한문제 이상 풀기")
                .vigibility(true)
                .authenticationType(AuthenticationType.PICTURE)
                .user(user)
                .build();
    }
}
