package com.likelion.devroutine.hashtag.repository;

import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.hashtag.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByContents(String hashTagContents);

    @Query(value = "SELECT * from hashtag h order by RAND() LIMIT 5", nativeQuery = true)
    List<HashTag> findHashTagsByRandom();
}
