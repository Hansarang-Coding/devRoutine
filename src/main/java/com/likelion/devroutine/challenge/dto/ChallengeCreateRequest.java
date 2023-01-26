package com.likelion.devroutine.challenge.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ChallengeCreateRequest {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "설명을 입력해주세요.")
    private String description;

    private boolean vigibility;
    private AuthenticationType authenticationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private LocalDate endDate;
    //해시태그 추가

    public boolean getVigibility() {
        return this.vigibility;
    }
}