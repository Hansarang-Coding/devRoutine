package com.likelion.devroutine.challenge.exception;

import com.likelion.devroutine.exception.ConflictException;

public class DuplicatedChallengeException extends ConflictException {
    private static final String MESSAGE = "이미 참여중인 챌린지입니다.";

    public DuplicatedChallengeException() {
        super(MESSAGE);
    }
}
