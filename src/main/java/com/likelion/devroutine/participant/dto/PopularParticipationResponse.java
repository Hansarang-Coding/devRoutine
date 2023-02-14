package com.likelion.devroutine.participant.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PopularParticipationResponse {
    private Long count;
    private Long challengeId;
    private boolean vigibility;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @QueryProjection
    public PopularParticipationResponse(Long count, Long challengeId, boolean vigibility, String title,
                                        String description, LocalDate startDate, LocalDate endDate) {
        this.count = count;
        this.challengeId = challengeId;
        this.vigibility = vigibility;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
