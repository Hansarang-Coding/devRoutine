package com.likelion.devroutine.auth.jwt;

import lombok.Getter;

@Getter
public class Token {
    private final String token;
    private final String refreshToken;

    public Token(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
