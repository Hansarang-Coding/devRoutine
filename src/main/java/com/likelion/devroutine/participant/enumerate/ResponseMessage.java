package com.likelion.devroutine.participant.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
    PARTICIPATE_SUCCESS("챌린지 참여 완료"),
    CHALLENGE_CANCEL_SUCCESS("챌린지 참여 취소 완료");

    private String message;
}
