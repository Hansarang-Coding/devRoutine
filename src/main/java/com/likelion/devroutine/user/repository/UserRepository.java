package com.likelion.devroutine.user.repository;

import com.likelion.devroutine.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByOauthId(String oauthId);
}
