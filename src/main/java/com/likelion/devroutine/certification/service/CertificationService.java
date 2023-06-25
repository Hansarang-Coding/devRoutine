package com.likelion.devroutine.certification.service;

import com.likelion.devroutine.certification.domain.Certification;
import com.likelion.devroutine.certification.dto.*;
import com.likelion.devroutine.certification.dto.github.UserEventDto;
import com.likelion.devroutine.certification.exception.CertificationForbiddenException;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ParticipationRepository participantRepository;
    private final UserRepository userRepository;
    private final GithubCrawl githubCrawl;

    @Transactional
    public CertificationCreateResponse createCertification(Long participationId, CertificationCreateRequest request,
                                                           String oauthId, String uploadImageUrl) {
        validateUserExists(oauthId);
        Certification certification = Certification.createCertification(AuthenticationType.PICTURE, uploadImageUrl,
                request.getDescription(), findParticipation(participationId));
        Certification savedCertification = certificationRepository.save(certification);
        return CertificationCreateResponse.of(savedCertification);
    }

    @Transactional
    public CertificationCreateResponse createCertification(Long participationId, CertificationGithubCreateRequest request,
                                                           String oauthId){
        validateUserExists(oauthId);
        Certification certification = Certification.createCertification(AuthenticationType.GITHUB, commitUrl(request.getGithubUrl()),
                request.getDescription(), findParticipation(participationId));
        Certification savedCertification = certificationRepository.save(certification);
        return CertificationCreateResponse.of(savedCertification);
    }

    // https://api.github.com/repos/{userName}/{reposName}/commits/{sha} => github.com/{userName}/{repoName}/commit/{sha}
    private String commitUrl(String githubUrl) {
        String url="";
        String[] parsingUrl=githubUrl.split("/");
        url=parsingUrl[0]+"//github.com/"+parsingUrl[4]+"/"+parsingUrl[5]+"/commit/"+parsingUrl[7];
        return url;
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

    public List<UserEventDto> getUserEvents(String oauthId){
        List<UserEventDto> userEventDtos=githubCrawl.getUserEvents(findUser(oauthId).getName());
        return userEventDtos;
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