package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.certification.domain.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class CertificationCreateResponse {
    private Long challengeId;
    private String description;
    private String imageUrl;

    public static CertificationCreateResponse of(Certification savedCertification) {
        return CertificationCreateResponse.builder()
                .challengeId(savedCertification.getParticipation().getChallenge().getId())
                .description(savedCertification.getParticipation().getChallenge().getDescription())
                .imageUrl(savedCertification.getImageUrl())
                .build();
    }

}
