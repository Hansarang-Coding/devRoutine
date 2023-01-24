package com.likelion.devroutine.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String picture;

    private String oauthId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;
        return this;
    }
}
