package com.likelion.devroutine.certification.repository;

import com.likelion.devroutine.certification.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByParticipationId(Long participationId);
}
