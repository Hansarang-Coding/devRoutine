package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.certification.domain.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CertificationDetailResponse {
    private String imageUrl;
    private String description;

    public static CertificationDetailResponse of(Certification certification) {
        return CertificationDetailResponse.builder()
                .imageUrl(certification.getUploadImageUrl())
                .description(certification.getDescription())
                .build();
    }
}
