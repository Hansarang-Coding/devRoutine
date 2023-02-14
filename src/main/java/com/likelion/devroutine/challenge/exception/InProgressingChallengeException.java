package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class InProgressingChallengeException extends BadRequestException {
    private static final String MESSAGE = "이미 진행중인 챌린지입니다.";

    public InProgressingChallengeException() {
        super(MESSAGE);
    }
}
