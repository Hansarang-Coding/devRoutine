package com.likelion.devroutine.auth.repository;

import com.likelion.devroutine.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
