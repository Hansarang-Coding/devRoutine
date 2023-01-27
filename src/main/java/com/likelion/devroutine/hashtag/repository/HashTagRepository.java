package com.likelion.devroutine.hashtag.repository;

import com.likelion.devroutine.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    List<HashTag> findByChallengeId(Long challengeId);

    @Query(value = "SELECT * from hashtag h order by RAND() LIMIT 5", nativeQuery = true)
    List<HashTag> findHashTagsByRandom();

}
