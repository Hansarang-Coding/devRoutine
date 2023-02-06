package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.certification.domain.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class CertificationResponse {
    private Long certificationId;
    private String challengeTitle;
    private String name;
    private LocalDate createdDate;
    private String uploadImageUrl;

    public static List<CertificationResponse> of(List<Certification> certifications){
        return certifications.stream()
                .map(certification -> CertificationResponse.builder()
                        .certificationId(certification.getId())
                        .challengeTitle(certification.getParticipation().getChallenge().getTitle())
                        .name(certification.getParticipation().getUser().getName())
                        .createdDate(certification.getCreatedAt().toLocalDate())
                        .uploadImageUrl(certification.getUploadImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
