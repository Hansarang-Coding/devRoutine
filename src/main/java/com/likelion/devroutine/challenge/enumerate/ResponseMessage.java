package com.likelion.devroutine.challenge.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    CHALLENGE_DELETE_SUCCESS("챌린지 삭제 완료"),
    CHALLENGE_MODIFY_SUCCESS("챌린지 수정 완료");

    private String message;
}
