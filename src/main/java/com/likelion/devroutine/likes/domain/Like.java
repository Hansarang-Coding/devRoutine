package com.likelion.devroutine.likes.domain;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certification_id")
    private Certification certification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id")
    private User user;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    public static Like createLike(User user, Certification certification) {
        return Like.builder()
                .user(user)
                .certification(certification)
                .build();
    }

}
