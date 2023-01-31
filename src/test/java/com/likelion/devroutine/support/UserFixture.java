package com.likelion.devroutine.support;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.domain.UserRole;

public class UserFixture {
    public static User getUser(){
        return User.builder()
                .id(1l)
                .oauthId("oauthId")
                .name("name")
                .role(UserRole.USER)
                .build();
    }
}
