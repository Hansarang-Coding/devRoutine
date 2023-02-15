package com.likelion.devroutine.certification.repository;

import com.likelion.devroutine.certification.domain.Certification;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificationRepositoryCustom {
    public List<Certification> findAllCertification();
}
