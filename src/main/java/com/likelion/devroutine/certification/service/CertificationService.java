package com.likelion.devroutine.certification.service;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.dto.*;
import com.likelion.devroutine.certification.repository.CertificationRepository;
import com.likelion.devroutine.likes.exception.CertificationNotFoundException;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.exception.ParticipationNotFoundException;
import com.likelion.devroutine.participant.repository.ParticipationRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ParticipationRepository participantRepository;
    private final UserRepository userRepository;

    @Transactional
    public CertificationCreateResponse createCertification(Long participationId, CertificationCreateRequest request,
                                                           String oauthId, String uploadImageUrl) {
        validateUserExists(oauthId);
        Certification certification = Certification.createCertification(uploadImageUrl,
                request.getDescription(), findParticipation(participationId));
        Certification savedCertification = certificationRepository.save(certification);
        return CertificationCreateResponse.of(savedCertification);
    }

    public CertificationFormResponse findCertificationFormInfo(Long participationId, String oauthId) {
        validateUserExists(oauthId);
        Participation participation = findParticipation(participationId);
        return CertificationFormResponse.of(participation);
    }

    private Participation findParticipation(Long participationId) {
        return participantRepository.findById(participationId)
                .orElseThrow(ParticipationNotFoundException::new);
    }

    public List<ParticipationResponse> findAllParticipationByUser(String oauthId) {
        User user = findUser(oauthId);
        List<Participation> participations = participantRepository.findAllByUserId(user.getId());
        return ParticipationResponse.of(participations);
    }

    private User findUser(String oauthId) {
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(UserNotFoundException::new);
    }

    public void validateUserExists(String oauthId) {
        if (!userRepository.existsByOauthId(oauthId))
            throw new UserNotFoundException();
    }

    public List<CertificationResponse> findCertifications() {
        List<Certification> certifications = certificationRepository.findAllCertification();
        return CertificationResponse.of(certifications);
    }

    public Certification findCertification(Long certificationId) {
        return certificationRepository.findById(certificationId)
                .orElseThrow(CertificationNotFoundException::new);
    }

    public CertificationDetailResponse findCertificationDetail(Long certificationId) {
        Certification certification = findCertification(certificationId);
        return CertificationDetailResponse.of(certification);
    }
}
