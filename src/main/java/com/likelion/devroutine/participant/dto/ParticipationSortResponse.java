package com.likelion.devroutine.participant.dto;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class ParticipationSortResponse {
    private Long count;
    private Long participationId;
    private Long challengeId;
    private String username;

    @QueryProjection
    public ParticipationSortResponse(Long count, Long participationId, Long challengeId, String username){
        this.count=count;
        this.participationId=participationId;
        this.challengeId=challengeId;
        this.username=username;
    }
}
