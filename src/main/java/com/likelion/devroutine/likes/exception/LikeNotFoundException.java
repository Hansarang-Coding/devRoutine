package com.likelion.devroutine.likes.exception;

import com.likelion.devroutine.exception.NotFoundException;

public class LikeNotFoundException extends NotFoundException {
    private static final String MESSAGE = "좋아요를 누른 상태가 아닙니다";

    public LikeNotFoundException() {
        super(MESSAGE);
    }
}
