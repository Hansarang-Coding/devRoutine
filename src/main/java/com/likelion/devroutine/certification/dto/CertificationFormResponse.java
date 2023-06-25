package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.participant.domain.Participation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Getter
public class CertificationFormResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private AuthenticationType authenticationType;

    public static CertificationFormResponse of(Participation participation){
        return CertificationFormResponse.builder()
                .id(participation.getId())
                .title(participation.getChallenge().getTitle())
                .description(participation.getChallenge().getDescription())
                .startDate(participation.getChallenge().getStartDate())
                .endDate(participation.getChallenge().getEndDate())
                .authenticationType(participation.getChallenge().getAuthenticationType())
                .build();
    }
}
