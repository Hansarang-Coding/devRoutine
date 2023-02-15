package com.likelion.devroutine.invite.exception;

import com.likelion.devroutine.exception.ConflictException;

public class DuplicatedInviteException extends ConflictException {
    private static final String MESSAGE = "이미 초대한 유저입니다.";

    public DuplicatedInviteException() {
        super(MESSAGE);
    }
}
