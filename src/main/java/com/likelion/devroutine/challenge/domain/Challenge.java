package com.likelion.devroutine.challenge.domain;

import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean vigibility;
    private Long fromUserId;
    @Enumerated(EnumType.STRING)
    private AuthenticationType authenticationType;
}
