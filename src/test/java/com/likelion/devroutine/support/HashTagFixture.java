package com.likelion.devroutine.support;

import com.likelion.devroutine.hashtag.domain.HashTag;

public class HashTagFixture {
    public static HashTag getHashTag(){
        return HashTag.builder()
                .id(1L)
                .contents("testHashTag")
                .build();
    }
}
