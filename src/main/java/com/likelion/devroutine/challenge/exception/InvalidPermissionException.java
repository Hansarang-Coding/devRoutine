package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.UnauthorizedException;

public class InvalidPermissionException extends UnauthorizedException {
    private static final String MESSAGE = "해당 챌린지에 접근 권한이 없습니다.";

    public InvalidPermissionException() {
        super(MESSAGE);
    }
}
