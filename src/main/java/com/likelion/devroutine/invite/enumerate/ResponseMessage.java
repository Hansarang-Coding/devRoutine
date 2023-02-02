package com.likelion.devroutine.invite.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {
    INVITE_SUCCESS("챌린지 초대 완료"),
    INVITE_ACCEPT("챌린지 초대 수락 완료"),
    INVITE_REJECT("챌린지 초대 거절 완료"),
    INVITE_CANCEL("챌린지 초대 취소 완료");
    private String message;
}
