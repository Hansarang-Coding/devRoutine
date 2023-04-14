package com.likelion.devroutine.certification.dto;

import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.participant.domain.Participation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
public class ParticipationResponse {
    private Long id;
    private String title;
    private String description;
    public static List<ParticipationResponse> of(List<Participation> participations) {
        return participations.stream()
                .map(participation -> ParticipationResponse.builder()
                        .id(participation.getId())
                        .title(participation.getChallenge().getTitle())
                        .description(participation.getChallenge().getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
