package com.likelion.devroutine.comment.domain;

import com.likelion.devroutine.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "certification_id")
//    private Certification certification;

    private Long certificationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //JPA Auditing
    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;


    public void setDeletedAt(LocalDateTime deletedAt){
        this.deletedAt = deletedAt;
    }



    public static Comment createComment(String comment, Long certificationId, User user) {
        return Comment.builder()
                .comment(comment)
                .certificationId(1L)
                .user(user)
                .build();
    }

    public void deleteComment(){
        this.setDeletedAt(LocalDateTime.now());
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }





}
