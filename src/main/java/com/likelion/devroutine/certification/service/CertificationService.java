package com.likelion.devroutine.certification.service;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.dto.CertificationCreateRequest;
import com.likelion.devroutine.certification.dto.CertificationCreateResponse;
import com.likelion.devroutine.certification.dto.CertificationFormResponse;
import com.likelion.devroutine.certification.dto.CertificationResponse;
import com.likelion.devroutine.certification.repository.CertificationRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
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
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    @Transactional
    public CertificationCreateResponse createCertification(Long challengeId, CertificationCreateRequest request,
                                                           String oauthId) {
        User user = findUser(oauthId);
        Challenge challenge = findChallenge(challengeId);
        Certification certification = Certification.createCertification(request.getCertImage().getOriginalFilename(),
                request.getDescription(), findParticipation(user, challenge));
        Certification savedCertification = certificationRepository.save(certification);
        return CertificationCreateResponse.of(savedCertification);
    }

    public CertificationFormResponse findParticipationInfo(Long challengeId, String oauthId) {
        User user = findUser(oauthId);
        Challenge challenge = findChallenge(challengeId);
        Participation participation = findParticipation(user, challenge);
        return CertificationFormResponse.of(participation);
    }

    private Participation findParticipation(User user, Challenge challenge) {
        return participantRepository.findByUserAndChallenge(user, challenge)
                .orElseThrow(ParticipationNotFoundException::new);
    }

    private Challenge findChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(ChallengeNotFoundException::new);
    }

    private User findUser(String oauthId) {
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(UserNotFoundException::new);
    }

    public List<CertificationResponse> findCertifications(Long challengeId, String oauthId) {
        Participation participation = findParticipation(findUser(oauthId), findChallenge(challengeId));
        List<Certification> certifications = certificationRepository.findByParticipationId(participation.getId());
        return CertificationResponse.of(certifications);
    }
}
