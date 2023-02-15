package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.certification.domain.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CertificationDetailResponse {
    private Long id;
    private Long userId;
    private String imageUrl;
    private String description;

    public static CertificationDetailResponse of(Certification certification) {
        return CertificationDetailResponse.builder()
                .id(certification.getId())
                .userId(certification.getParticipation().getUser().getId())
                .imageUrl(certification.getUploadImageUrl())
                .description(certification.getDescription())
                .build();
    }
}
