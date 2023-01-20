package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.UnauthorizedException;

public class InaccessibleChallengeException extends UnauthorizedException {
    private static final String MESSAGE = "해당 챌린지에 접근할 수 없습니다.";

    public InaccessibleChallengeException() {
        super(MESSAGE);
    }
}
