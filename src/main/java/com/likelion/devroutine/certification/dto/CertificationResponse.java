package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.certification.domain.Certification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class CertificationResponse {
    private String imageUrl;

    public static List<CertificationResponse> of(List<Certification> certifications){
        return certifications.stream()
                .map(certification -> CertificationResponse.builder()
                        .imageUrl(certification.getImageUrl())
                        .build())
                .collect(Collectors.toList());
    }
}