package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.ForbiddenException;

public class InaccessibleChallengeException extends ForbiddenException {
    private static final String MESSAGE = "비공개 챌린지입니다.";

    public InaccessibleChallengeException() {
        super(MESSAGE);
    }
}
