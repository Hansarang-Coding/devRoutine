package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.UnauthorizedException;

public class InvalidPermissionException extends UnauthorizedException {
    private static final String MESSAGE = "해당 챌린지 생성자가 아닙니다.";

    public InvalidPermissionException() {
        super(MESSAGE);
    }
}
