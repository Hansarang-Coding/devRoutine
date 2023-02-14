package com.likelion.devroutine.invite.exception;

import com.likelion.devroutine.exception.NotFoundException;
public class InviteNotFoundException extends NotFoundException {
    private static final String MESSAGE ="해당 초대 기록이 존재하지 않습니다";

    public InviteNotFoundException() {
        super(MESSAGE);
    }
}
