package com.likelion.devroutine.auth.exception;

import com.likelion.devroutine.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당 사용자를 찾을 수 없습니다.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
