package com.likelion.devroutine.participant.exception;

import com.likelion.devroutine.exception.ForbiddenException;

public class RejectCancelException extends ForbiddenException {
    private static final String MESSAGE = "챌린지 생성자는 챌린지 참여 취소를 할 수 없습니다.";

    public RejectCancelException() {
        super(MESSAGE);
    }
}
