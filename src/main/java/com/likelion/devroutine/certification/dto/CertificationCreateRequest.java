package com.likelion.devroutine.certification.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class CertificationCreateRequest {
    @NotNull(message = "인증 이미지를 입력해 주세요.")
    private MultipartFile certImage;

    @NotEmpty(message = "챌린지 설명을 입력해 주세요.")
    private String description;
}
