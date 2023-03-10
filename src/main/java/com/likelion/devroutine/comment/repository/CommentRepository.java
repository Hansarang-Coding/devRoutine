package com.likelion.devroutine.comment.repository;

import com.likelion.devroutine.comment.domain.Comment;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByCertificationId(Long certificationId, Pageable pageable);

    @Query("SELECT p.user FROM Comment c INNER JOIN c.certification cc INNER JOIN " +
            "cc.participation p ON c.certification.id = :id")
    User findUserByCommentParam(@Param("id") Long id);

}
