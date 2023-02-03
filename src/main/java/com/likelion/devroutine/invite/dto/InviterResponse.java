package com.likelion.devroutine.invite.dto;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class InviterResponse {
    private Long challengeId;
    private String title;

    public static List<InviterResponse> toList(List<Challenge> challenges) {
        return challenges.stream().map(challenge-> InviterResponse.builder()
                        .challengeId(challenge.getId())
                        .title(challenge.getTitle())
                .build())
                .collect(Collectors.toList());
    }
}
