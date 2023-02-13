package com.likelion.devroutine.follow.repository;

import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingUserId);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingUserId);

    List<Follow> findByFollowingId(Long followingUserId);

    List<Follow> findByFollowerId(Long followerId);

    @Query("SELECT count(f) FROM Follow f where f.follower.id = :followerId")
    Long countFollowers(@Param("followerId") Long followerId);

    @Query("SELECT count(f) FROM Follow f where f.following.id = :followingId")
    Long countFollowing(@Param("followingId") Long followingUserId);

    @Query("SELECT ff FROM Follow f INNER JOIN f.follower ff ON f.following.id = :id")
    List<User> findByFollowerUserId(@Param("id") Long userId);

    @Query("SELECT ff FROM Follow f INNER JOIN f.following ff ON f.follower.id = :id")
    List<User> findByFollowingUserId(@Param("id") Long userId);
}
