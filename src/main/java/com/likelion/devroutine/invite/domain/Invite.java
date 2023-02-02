package com.likelion.devroutine.invite.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Where(clause="deleted_at is NULL")
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long inviterId;
    private Long inviteeId;
    private Long challengeId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    public static Invite createInvite(Long challengeId, Long inviterId, Long inviteeId) {
        return Invite.builder()
                .challengeId(challengeId)
                .inviterId(inviterId)
                .inviteeId(inviteeId)
                .build();
    }

    public void deleteChallenge(){
        this.deletedAt=LocalDateTime.now();
    }
}
