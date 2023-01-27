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
import com.likelion.devroutine.hashtag.domain.HashTag;
import com.likelion.devroutine.hashtag.repository.HashTagRepository;
import com.likelion.devroutine.keyword.domain.Keyword;
import com.likelion.devroutine.keyword.repository.KeywordRepository;
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
    private final KeywordRepository keywordRepository;
    private final HashTagRepository hashTagRepository;
    private final UserRepository userRepository;

    public ChallengeService(
            ChallengeRepository challengeRepository,
            KeywordRepository keywordRepository,
            HashTagRepository hashTagRepository,
            UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.keywordRepository = keywordRepository;
        this.hashTagRepository = hashTagRepository;
        this.userRepository = userRepository;
    }

    public List<ChallengeDto> findAllChallenge(Long challengeId, int size) {
        List<Challenge> challenges = challengeRepository.findAllSortById(challengeId, PageRequest.of(0, size));
        List<HashTag> hashTags = hashTagRepository.findHashTagsByRandom();
        return ChallengeDto.toList(challenges, hashTags);
    }

    public List<ChallengeDto> findAllChallengeTitle(Long challengeId, int size, String keyword) {
        List<Challenge> challenges = challengeRepository.findSearchTitleSortById(challengeId, keyword, PageRequest.of(0, size));
        List<HashTag> hashTags = hashTagRepository.findByChallengeId(challengeId);
        return ChallengeDto.toList(challenges, hashTags);
    }

    public ChallengeDto findByChallengeId(Long id) {
        Challenge challenge = getChallenge(id);
        isVigibility(challenge);
        List<HashTag> hashTags = hashTagRepository.findByChallengeId(id);
        return ChallengeDto.toDto(challenge, hashTags);
    }

    @Transactional
    public ChallengeCreateResponse createChallenge(String oauthId, ChallengeCreateRequest dto) {
        User user = getUser(oauthId);
        Challenge savedChallenge = challengeRepository.save(Challenge.createChallenge(user, dto));
        List<String> keywordList = extractKeywords(dto.getKeyword());
        saveHashTagAndKeyword(savedChallenge, keywordList);
        return ChallengeCreateResponse.toResponse(savedChallenge, keywordList);
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
    public ChallengeResponse modifyChallenge(String oauthId, Long id, ChallengeModifiyRequest dto) {
        User user = getUser(oauthId);
        Challenge challenge = getChallenge(id);

        matchWriterAndUser(challenge, user);

        isProgressChallenge(challenge.getStartDate());
        challenge.updateChallenge(dto);

        //해시태그 수정 기능 추가

        return ChallengeResponse.builder()
                .message(ResponseMessage.CHALLENGE_MODIFY_SUCCESS.getMessage()).build();
    }

    private void saveHashTagAndKeyword(Challenge challenge, List<String> keywordList) {
        for (String keywordContent : keywordList) {
            Keyword savedKeyword = keywordRepository.findByContents(keywordContent)
                    .orElseGet(() -> keywordRepository.save(Keyword.createKeyword(keywordContent)));
            hashTagRepository.save(HashTag.createHashTag(challenge, savedKeyword));
        }
    }

    /**
     * 여러 개의 키워드가 입력되는 경우 이를 분리해준다
     * e.g #Spring #강의 #김영한 #인프런
     * Spring 강의 김영한 인프런 4개의 키워드로 분리시킬 수 있다.
     * @param keywords
     * @return
     */
    private List<String> extractKeywords(String keywords) {
        return Arrays.stream(keywords.split("#"))
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
