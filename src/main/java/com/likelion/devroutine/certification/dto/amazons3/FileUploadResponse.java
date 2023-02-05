package com.likelion.devroutine.certification.dto.amazons3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FileUploadResponse {
    private String fileName;
    private String uploadImageUrl;
}
