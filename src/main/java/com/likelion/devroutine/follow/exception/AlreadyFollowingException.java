package com.likelion.devroutine.follow.exception;

import com.likelion.devroutine.exception.ConflictException;

public class AlreadyFollowingException extends ConflictException {
    private static final String MESSAGE = "이미 팔로잉 하고 있는 유저 입니다.";

    public AlreadyFollowingException() {
        super(MESSAGE);
    }
}
