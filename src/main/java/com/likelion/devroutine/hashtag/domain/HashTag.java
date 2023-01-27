package com.likelion.devroutine.hashtag.domain;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.keyword.domain.Keyword;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "hashtag")
@Entity
public class HashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;

    public static HashTag createHashTag(Challenge challenge, Keyword keyword) {
        return HashTag.builder()
                .challenge(challenge)
                .keyword(keyword)
                .build();
    }
}
