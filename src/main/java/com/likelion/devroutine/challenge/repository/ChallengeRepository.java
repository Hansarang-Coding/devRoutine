package com.likelion.devroutine.challenge.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge, Long>, ChallengeRepositoryCustom {
    List<Challenge> findAllByUserId(Long userId);
}
