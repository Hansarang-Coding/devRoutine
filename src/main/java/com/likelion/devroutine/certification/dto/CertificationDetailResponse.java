package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CertificationDetailResponse {
    private Long id;
    private Long userId;
    private String uploadUrl;
    private String description;
    private AuthenticationType authenticationType;

    public static CertificationDetailResponse of(Certification certification) {
        return CertificationDetailResponse.builder()
                .id(certification.getId())
                .userId(certification.getParticipation().getUser().getId())
                .uploadUrl(certification.getUploadUrl())
                .description(certification.getDescription())
                .authenticationType(certification.getAuthenticationType())
                .build();
    }
}
