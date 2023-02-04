package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.participant.domain.Participation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class CertificationListResponse {
    private Long id;
    private String title;
    private String description;

    public static List<CertificationListResponse> of(List<Participation> participations) {
        return participations.stream()
                .map(participation -> CertificationListResponse.builder()
                        .id(participation.getId())
                        .title(participation.getChallenge().getTitle())
                        .description(participation.getChallenge().getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
