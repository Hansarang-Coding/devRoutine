package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.NotFoundException;

public class ChallengeNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당 챌린지를 찾을 수 없습니다.";

    public ChallengeNotFoundException() {
        super(MESSAGE);
    }
}
