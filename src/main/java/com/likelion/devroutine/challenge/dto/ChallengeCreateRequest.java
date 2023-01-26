package com.likelion.devroutine.challenge.dto;

import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ChallengeCreateRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "설명을 입력해주세요.")
    private String description;

    private boolean isVigibility;
    private AuthenticationType authenticationType;
    //해시태그 추가

    public boolean getIsVigibility(){
        return this.isVigibility;
    }
}
