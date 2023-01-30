package com.likelion.devroutine.comment.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ResponseMessage {
    COMMENT_DELETE_SUCCESS("댓글 삭제 완료"),
    COMMENT_MODIFY_SUCCESS("댓글 수정 완료");

    private String message;
}
