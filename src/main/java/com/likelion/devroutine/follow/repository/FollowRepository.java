package com.likelion.devroutine.follow.repository;

import com.likelion.devroutine.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingUserId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingUserId);

    List<Follow> findByFollowingId(Long followingUserId);

    List<Follow> findByFollowerId(Long followerId);
}
