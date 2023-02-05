package com.likelion.devroutine.invite.dto;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.user.domain.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor
public class InviterResponse {
    private Long challengeId;
    private String title;
    private String inviteeName;
    private String inviteePicture;

    @QueryProjection
    public InviterResponse(Long challengeId, String title, String inviteeName, String inviteePicture) { // 생성자
        this.challengeId=challengeId;
        this.title=title;
        this.inviteeName=inviteeName;
        this.inviteePicture=inviteePicture;
    }

    public static List<InviterResponse> toList(List<Challenge> challenges) {
        return challenges.stream().map(challenge-> InviterResponse.builder()
                        .challengeId(challenge.getId())
                        .title(challenge.getTitle())
                .build())
                .collect(Collectors.toList());
    }
}
