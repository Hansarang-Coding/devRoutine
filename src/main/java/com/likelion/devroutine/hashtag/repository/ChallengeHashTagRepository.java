package com.likelion.devroutine.hashtag.repository;

import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChallengeHashTagRepository extends JpaRepository<ChallengeHashTag, Long> {
    List<ChallengeHashTag> findByChallengeId(Long challengeId);

    @Query(value = "SELECT * from challenge_hashtag h order by RAND() LIMIT 5", nativeQuery = true)
    List<ChallengeHashTag> findHashTagsByRandom();

}
