package com.likelion.devroutine.challenge.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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

import java.time.LocalDate;
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
    private boolean vigibility;
    private Long fromUserId; //user와 FK로 추후에 수정
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;

    private LocalDate startDate;
    private LocalDate endDate;


    //사용자 id 추가
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    public boolean getVigibility(){
        return this.vigibility;
    }
    public static Challenge createChallenge(ChallengeCreateRequest dto) {
        return Challenge.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .vigibility(dto.getVigibility())
                .authenticationType(dto.getAuthenticationType())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .fromUserId(1l)
                .build();
    }
    public void deleteChallenge(){
        this.deletedAt=LocalDateTime.now();
    }

    public void updateChallenge(ChallengeModifiyRequest dto) {
        this.title=dto.getTitle();
        this.description=dto.getDescription();
        this.vigibility=dto.getVigibility();
        this.authenticationType=dto.getAuthenticationType();
    }
}
