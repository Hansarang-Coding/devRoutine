package com.likelion.devroutine.follow.exception;

import com.likelion.devroutine.exception.NotFoundException;

public class FollowingNotFoundException extends NotFoundException {

    private static final String MESSAGE ="해당 팔로우가 존재하지 않습니다";

    public FollowingNotFoundException() {
        super(MESSAGE);
    }

}
