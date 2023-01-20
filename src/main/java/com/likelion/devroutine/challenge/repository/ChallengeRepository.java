package com.likelion.devroutine.challenge.repository;

import com.likelion.devroutine.challenge.domain.Challenge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    Slice<Challenge> findByIdLessThanAndTitleContainingOrderByIdDesc(Long id, String keyword, Pageable pageable);
    Slice<Challenge> findByIdLessThanOrderByIdDesc(Long id, Pageable pageable);
}
