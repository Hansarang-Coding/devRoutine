package com.likelion.devroutine.comment.repository;

import com.likelion.devroutine.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByCertificationId(Long certificationId, Pageable pageable);

}
