package com.likelion.devroutine.hashtag.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "hashtag")
@Entity
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    public static HashTag createHashTag(String hashTagContents) {
        return HashTag.builder()
                .contents(hashTagContents)
                .build();
    }
}
