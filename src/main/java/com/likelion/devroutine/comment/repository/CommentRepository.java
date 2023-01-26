package com.likelion.devroutine.comment.repository;

import com.likelion.devroutine.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
