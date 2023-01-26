package com.likelion.devroutine.challenge.domain;

import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean isVigibility;
    private Long fromUserId;
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

    //사용자 id 추가
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public boolean getIsVigibility(){
        return this.isVigibility;
    }
    public static Challenge createChallenge(ChallengeCreateRequest dto) {
        return Challenge.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .isVigibility(dto.getIsVigibility())
                .authenticationType(dto.getAuthenticationType())
                .fromUserId(1l)
                .build();
    }
}
