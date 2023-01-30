package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.exception.UserNotFoundException;
import com.likelion.devroutine.auth.repository.UserRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.enumerate.ResponseMessage;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.hashtag.domain.ChallengeHashTag;
import com.likelion.devroutine.hashtag.domain.HashTag;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import com.likelion.devroutine.hashtag.repository.ChallengeHashTagRepository;
import com.likelion.devroutine.hashtag.repository.HashTagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final HashTagRepository hashTagRepository;
    private final ChallengeHashTagRepository challengeHashTagRepository;
    private final UserRepository userRepository;

    public ChallengeService(
            ChallengeRepository challengeRepository,
            HashTagRepository hashTagRepository,
            ChallengeHashTagRepository challengeHashTagRepository,
            UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.hashTagRepository = hashTagRepository;
        this.challengeHashTagRepository = challengeHashTagRepository;
        this.userRepository = userRepository;
    }

    public List<ChallengeDto> findAllChallenge(Long challengeId, int size) {
        List<Challenge> challenges = challengeRepository.findAllSortById(challengeId, PageRequest.of(0, size));
        List<ChallengeHashTag> challengeHashTags = challengeHashTagRepository.findHashTagsByRandom();
        return ChallengeDto.toList(challenges, ChallengeHashTagResponse.of(challengeHashTags));
    }

    public List<ChallengeDto> findAllChallengeTitle(Long challengeId, int size, String keyword) {
        List<Challenge> challenges = challengeRepository.findSearchTitleSortById(challengeId, keyword, PageRequest.of(0, size));
        List<ChallengeHashTag> challengeHashTags = getHashTags(challengeId);
        return ChallengeDto.toList(challenges, ChallengeHashTagResponse.of(challengeHashTags));
    }

    public ChallengeDto findByChallengeId(Long challengeId) {
        Challenge challenge = getChallenge(challengeId);
        isVigibility(challenge);
        return ChallengeDto.toDto(challenge, ChallengeHashTagResponse.of(challenge.getChallengeHashTags()));
    }

    @Transactional
    public ChallengeCreateResponse createChallenge(String oauthId, ChallengeCreateRequest dto) {
        User user = getUser(oauthId);
        Challenge savedChallenge = challengeRepository.save(Challenge.createChallenge(user, dto));
        List<String> hashTags = extractHashTag(dto.getHashTag());
        saveNewHashTags(savedChallenge.getId(), hashTags);
        return ChallengeCreateResponse.toResponse(savedChallenge, hashTags);
    }

    @Transactional
    public ChallengeResponse deleteChallenge(String oauthId, Long id) {
        User user = getUser(oauthId);
        Challenge challenge = getChallenge(id);

        matchWriterAndUser(challenge, user);

        //챌린지 시작 전인지 확인
        isProgressChallenge(challenge.getStartDate());

        challenge.deleteChallenge();
        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_DELETE_SUCCESS.getMessage()).build();
    }

    @Transactional
    public ChallengeResponse modifyChallenge(String oauthId, Long challengeId, ChallengeModifiyRequest dto) {
        User user = getUser(oauthId);
        Challenge challenge = getChallenge(challengeId);
        matchWriterAndUser(challenge, user);
        isProgressChallenge(challenge.getStartDate());
        challenge.updateChallenge(dto);
        removeChallengeHashTag(challenge);
        saveNewHashTags(challengeId, extractHashTag(dto.getHashtag()));
        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_MODIFY_SUCCESS.getMessage()).build();
    }

    private void removeChallengeHashTag(Challenge challenge) {
        List<ChallengeHashTag> challengeHashTags = challenge.getChallengeHashTags();
        challengeHashTagRepository.deleteAll(challengeHashTags);
        challengeHashTags.clear();
    }

    private void saveNewHashTags(Long challengeId, List<String> hashTags) {
        Challenge challenge = getChallenge(challengeId);
        for (String hashTagContents : hashTags) { //#자바 #김영한
            HashTag savedHashTag = hashTagRepository.findByContents(hashTagContents)
                    .orElseGet(() -> hashTagRepository.save(HashTag.createHashTag(hashTagContents)));
            challengeHashTagRepository.save(ChallengeHashTag.create(challenge, savedHashTag));
        }
    }

    private List<ChallengeHashTag> getHashTags(Long challengeId) {
        return challengeHashTagRepository.findByChallengeId(challengeId);
    }

    public List<Challenge> findAll() {
        return challengeRepository.findAll();
    }

    private List<String> extractHashTag(String hashTag) {
        return Arrays.stream(hashTag.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());
    }

    public Challenge getChallenge(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new ChallengeNotFoundException());
        return challenge;
    }

    public boolean isVigibility(Challenge challenge) {
        if (challenge.getVigibility()) return true;
        else throw new InaccessibleChallengeException();
    }

    public boolean isProgressChallenge(LocalDate startDate) {
        if (LocalDate.now().isAfter(startDate))
            throw new InProgressingChallengeException();
        return false;
    }

    public User getUser(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserNotFoundException());
        return user;
    }

    public boolean matchWriterAndUser(Challenge challenge, User user) {
        if (!user.getId().equals(challenge.getUser().getId())) {
            throw new InvalidPermissionException();
        }
        return true;
    }
}
