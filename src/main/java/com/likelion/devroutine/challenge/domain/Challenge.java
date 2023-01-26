package com.likelion.devroutine.challenge.domain;

import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.dto.ChallengeModifiyRequest;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
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
@Where(clause="deleted_at is NULL")
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean isVigibility;
    private Long fromUserId; //user와 FK로 추후에 수정
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

    //사용자 id 추가
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private LocalDateTime deletedAt;

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
    public void deleteChallenge(){
        this.deletedAt=LocalDateTime.now();
    }

    public void updateChallenge(ChallengeModifiyRequest dto) {
        this.title=dto.getTitle();
        this.description=dto.getDescription();
        this.isVigibility=dto.getIsVigibility();
        this.authenticationType=dto.getAuthenticationType();
    }
}
