package com.likelion.devroutine.auth.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class OAuth2RegistrationException extends BadRequestException {

    private static final String MESSAGE = "해당 소셜 로그인은 지원 하지 않습니다";

    public OAuth2RegistrationException() {
        super(MESSAGE);
    }

    public OAuth2RegistrationException(String message){
        super(message);
    }
}
