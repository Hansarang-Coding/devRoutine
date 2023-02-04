package com.likelion.devroutine.certification.domain;

import com.likelion.devroutine.participant.domain.Participation;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Entity
public class Certification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadImageUrl;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participation_id")
    private Participation participation;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    public static Certification createCertification(String uploadImageUrl, String description, Participation participation) {
        return Certification.builder()
                .uploadImageUrl(uploadImageUrl)
                .description(description)
                .participation(participation)
                .build();
    }
}
