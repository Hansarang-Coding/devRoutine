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
public class InviteeResponse {
    private Long inviteId;
    private Long challengeId;
    private String title;
    private String inviteeName;
    private String inviteePicture;

    @QueryProjection
    public InviteeResponse(Long inviteId, Long challengeId, String title, String inviteeName, String inviteePicture) { // 생성자
        this.inviteId=inviteId;
        this.challengeId=challengeId;
        this.title=title;
        this.inviteeName=inviteeName;
        this.inviteePicture=inviteePicture;
    }
}
