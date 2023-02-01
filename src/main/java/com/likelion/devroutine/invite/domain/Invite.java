package com.likelion.devroutine.invite.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long inviterId;
    private Long inviteeId;
    private Long challengeId;

    public static Invite createInvite(Long challengeId, Long inviterId, Long inviteeId) {
        return Invite.builder()
                .challengeId(challengeId)
                .inviterId(inviterId)
                .inviteeId(inviteeId)
                .build();
    }
}
