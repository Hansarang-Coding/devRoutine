package com.likelion.devroutine.participant.exception;

import com.likelion.devroutine.exception.NotFoundException;

public class ParticipationNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당 챌린지를 참여신청한 기록이 없습니다.";

    public ParticipationNotFoundException() {
        super(MESSAGE);
    }
}
