package com.likelion.devroutine.certification.service;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.dto.*;
import com.likelion.devroutine.certification.dto.github.CommitApiDto;
import com.likelion.devroutine.certification.dto.github.CommitDto;
import com.likelion.devroutine.certification.dto.github.RepositoryDto;
import com.likelion.devroutine.certification.exception.CertificationForbiddenException;
import com.likelion.devroutine.certification.exception.NotGithubAuthenticationException;
import com.likelion.devroutine.certification.repository.CertificationRepository;
import com.likelion.devroutine.certification.util.GithubCrawl;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.challenge.exception.NotStartingChallengeException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ParticipationRepository participantRepository;
    private final UserRepository userRepository;

    private final GithubCrawl githubCrawl;

    @Transactional
    public CertificationCreateResponse createCertification(Long participationId, CertificationCreateRequest request,
                                                           String oauthId, String uploadImageUrl) {
        validateUserExists(oauthId);
        Certification certification = Certification.createCertification(uploadImageUrl,
                request.getDescription(), findParticipation(participationId));
        Certification savedCertification = certificationRepository.save(certification);
        return CertificationCreateResponse.of(savedCertification);
    }

    private void validateCertificationDate(Long participationId, String oauthId) {
        List<Certification> certifications = certificationRepository.findByParticipationId(participationId);
        certifications.stream().filter(certification -> isCertificatedUser(oauthId, certification))
                .filter(this::isCertificatedNotBeenOneDay).forEachOrdered(certification -> {
                    throw new CertificationForbiddenException();
                });
    }

    private boolean isCertificatedUser(String oauthId, Certification certification) {
        return Objects.equals(certification.getParticipation().getUser().getName(), findUser(oauthId).getName());
    }

    private boolean isCertificatedNotBeenOneDay(Certification certification) {
        return certification.getCreatedAt().toLocalDate().isEqual(LocalDate.now());
    }

    public CertificationFormResponse findCertificationFormInfo(Long participationId, String oauthId) {
        validateUserExists(oauthId);
        isProgressing(participationId);
        validateCertificationDate(participationId, oauthId);
        Participation participation = findParticipation(participationId);

        return CertificationFormResponse.of(participation);
    }

    public List<RepositoryDto> getUserRepository(Long participationId, String oauthId){
        validateUserExists(oauthId);
        isProgressing(participationId);
        validateCertificationDate(participationId, oauthId);
        Participation participation = findParticipation(participationId);
        List<RepositoryDto> repos;
        if(participation.getChallenge().getAuthenticationType().equals(AuthenticationType.GITHUB)) {
            repos=githubCrawl.getUserRepository(participation.getUser().getName());
        }else{
            throw new NotGithubAuthenticationException();
        }
        return repos;
    }

    public List<CommitApiDto> getRepositoryCommits(String repoName, Long participationId, String oauthId){
        validateUserExists(oauthId);
        isProgressing(participationId);
        validateCertificationDate(participationId, oauthId);
        Participation participation = findParticipation(participationId);
        return githubCrawl.getUserCommit(repoName, participation.getUser().getName());
    }

    private void isProgressing(Long participationId) {
        Challenge challenge = participantRepository.findById(participationId).get().getChallenge();
        if(LocalDate.now().isBefore(challenge.getStartDate())) throw new NotStartingChallengeException();
    }

    private Participation findParticipation(Long participationId) {
        return participantRepository.findById(participationId)
                .orElseThrow(ParticipationNotFoundException::new);
    }

    public List<ParticipationResponse> findAllParticipationByUser(String oauthId) {
        User user = findUser(oauthId);
        List<Participation> participations = participantRepository.findProgressingChallengeByUserId(user.getId());
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