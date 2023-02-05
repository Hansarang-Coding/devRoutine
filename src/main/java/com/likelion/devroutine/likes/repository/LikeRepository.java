package com.likelion.devroutine.likes.repository;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.likes.domain.Like;
import com.likelion.devroutine.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByCertificationAndUser(Certification certification, User user);

    Integer countByCertification(Certification certification);

    boolean existsByCertificationIdAndUserId(Long certificationId, Long userId);
}
