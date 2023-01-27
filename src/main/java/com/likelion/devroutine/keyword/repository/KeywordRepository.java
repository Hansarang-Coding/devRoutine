package com.likelion.devroutine.keyword.repository;

import com.likelion.devroutine.keyword.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Optional<Keyword> findByContents(String contents);
}
