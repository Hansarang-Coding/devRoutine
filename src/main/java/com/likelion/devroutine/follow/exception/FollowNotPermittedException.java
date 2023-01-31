package com.likelion.devroutine.follow.exception;

import com.likelion.devroutine.exception.BadRequestException;

public class FollowNotPermittedException extends BadRequestException {
    private static final String MESSAGE = "자기 자신은 팔로우 할 수 없습니다.";

    public FollowNotPermittedException() {
        super(MESSAGE);
    }
}
