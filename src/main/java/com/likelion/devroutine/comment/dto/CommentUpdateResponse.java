package com.likelion.devroutine.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.devroutine.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentUpdateResponse {

    private Long id;
    private String comment;
    private String userName;
    private Long certificationId;

    private LocalDateTime lastModifiedAt;


    public static CommentUpdateResponse of(Comment savedComment) {
        return CommentUpdateResponse.builder()
                .id(savedComment.getId())
                .comment(savedComment.getComment())
                .userName(savedComment.getUser().getName())
                //.certificationId(savedComment.getCertification().getId())
                .lastModifiedAt(savedComment.getModifiedAt())
                .build();
    }
}
