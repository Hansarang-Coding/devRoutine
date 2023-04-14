package com.likelion.devroutine.certification.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class NotGithubAuthenticationException extends BadRequestException {
    private static final String MESSAGE = "Github 인증 방식이 아닙니다.";

    public NotGithubAuthenticationException() {
        super(MESSAGE);
    }
}
