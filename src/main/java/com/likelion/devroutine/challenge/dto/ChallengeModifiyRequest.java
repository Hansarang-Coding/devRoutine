package com.likelion.devroutine.challenge.dto;

import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ChallengeModifiyRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "설명을 입력해주세요.")
    private String description;

    private boolean vigibility;
    private AuthenticationType authenticationType;

    //해시태그 추가
    private String keyword;

    public boolean getVigibility(){
        return this.vigibility;
    }
}
