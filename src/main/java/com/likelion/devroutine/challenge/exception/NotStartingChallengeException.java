package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class NotStartingChallengeException extends BadRequestException {
    private static final String MESSAGE = "시작 전인 챌린지입니다.";

    public NotStartingChallengeException() {
        super(MESSAGE);
    }
}
