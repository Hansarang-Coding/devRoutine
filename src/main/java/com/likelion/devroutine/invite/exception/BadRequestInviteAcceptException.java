package com.likelion.devroutine.invite.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class BadRequestInviteAcceptException extends BadRequestException {
    private static final String MESSAGE ="잘못된 접근입니다.";

    public BadRequestInviteAcceptException() {
        super(MESSAGE);
    }
}
