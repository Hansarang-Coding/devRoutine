package com.likelion.devroutine.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentRequest {

    @NotEmpty(message = "댓들을 입력해주세요.")
    private String comment;

}
