package com.likelion.devroutine.certification.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class CertificationGithubCreateRequest {
    @NotNull(message = "github commmit url을 입력해 주세요.")
    private String githubUrl;

    @NotEmpty(message = "챌린지 설명을 입력해 주세요.")
    private String description;
}
