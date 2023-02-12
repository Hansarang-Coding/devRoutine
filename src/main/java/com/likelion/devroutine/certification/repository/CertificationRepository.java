package com.likelion.devroutine.certification.repository;

import com.likelion.devroutine.certification.domain.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByParticipationIdOrderByCreatedAtDesc(Long participationId);

    @Query("select c from Certification c order by c.createdAt desc")
    List<Certification> findAllCertification();

    List<Certification> findByParticipationId(Long participationId);
}
