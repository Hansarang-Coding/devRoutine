package com.likelion.devroutine.user.exception;

import com.likelion.devroutine.exception.UnauthorizedException;

public class UserUnauthorizedException extends UnauthorizedException {
    private final static String MESSAGE = "해당 댓글을 삭제할 수 있는 권한이 없습니다.";
    public UserUnauthorizedException() {
        super(MESSAGE);
    }
}
