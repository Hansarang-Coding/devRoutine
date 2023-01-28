package com.likelion.devroutine.hashtag.domain;

import com.likelion.devroutine.challenge.domain.Challenge;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Table(name = "challenge_hashtag")
@Entity
public class ChallengeHashTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    private HashTag hashTag;

    public static ChallengeHashTag create(Challenge challenge, HashTag hashTag) {
        return ChallengeHashTag.builder()
                .challenge(challenge)
                .hashTag(hashTag)
                .build();
    }
}
