package com.likelion.devroutine.participant.domain;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="challenge_id")
    private Challenge challenge;

    public static Participation createParticipant(User user, Challenge challenge){
        return Participation.builder()
                .user(user)
                .challenge(challenge)
                .build();
    }
}
