package com.likelion.devroutine.keyword.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Keyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    public static Keyword createKeyword(String hashTagContents) {
        return Keyword.builder()
                .contents(hashTagContents)
                .build();
    }
}
