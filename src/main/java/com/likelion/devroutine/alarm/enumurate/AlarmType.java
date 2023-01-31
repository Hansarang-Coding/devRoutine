package com.likelion.devroutine.alarm.enumurate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlarmType {
    NEW_CHALLENGE_INVITE("new challenge invite"),
    NEW_COMMENT_ON_CERTIFICATION("new comment"),
    NEW_LIKE_ON_CERTIFICATION("new like"),
    NEW_FOLLOWER_("new follower");


    private final String message;
}
