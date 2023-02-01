package com.likelion.devroutine.invite.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseMessage {
    INVITE_SUCCESS("챌린지 초대 완료");

    private String message;
}
