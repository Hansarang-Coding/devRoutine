package com.likelion.devroutine.challenge.repository;


import com.likelion.devroutine.challenge.domain.Challenge;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChallengeRepositoryCustom {
    List<Challenge> findSearchTitleSortById(Long id, String keyword, Pageable pageable);
    List<Challenge> findAllSortById(Long id, Pageable pageable);
}
