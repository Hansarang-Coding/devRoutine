package com.likelion.devroutine.participant.exception;

import com.likelion.devroutine.exception.ConflictException;

public class DuplicatedParticipationException extends ConflictException {
    private static final String MESSAGE = "이미 참여중인 챌린지입니다.";

    public DuplicatedParticipationException() {
        super(MESSAGE);
    }
}
