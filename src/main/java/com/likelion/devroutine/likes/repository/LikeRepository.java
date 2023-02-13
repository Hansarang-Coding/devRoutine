package com.likelion.devroutine.likes.repository;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.likes.domain.Like;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByCertificationAndUser(Certification certification, User user);

    Integer countByCertification(Certification certification);

    boolean existsByCertificationIdAndUserId(Long certificationId, Long userId);

    @Query("SELECT p.user FROM Like l INNER JOIN l.certification c INNER JOIN " +
            "c.participation p ON l.certification.id = :id")
    User findUserByLikeParam(@Param("id") Long id);
}
