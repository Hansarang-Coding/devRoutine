package com.likelion.devroutine.challenge.service;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.auth.domain.UserRole;
import com.likelion.devroutine.auth.repository.UserRepository;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.dto.ChallengeDto;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InaccessibleChallengeException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.hashtag.repository.HashTagRepository;
import com.likelion.devroutine.keyword.repository.KeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ChallengeServiceTest {
    private ChallengeService challengeService;
    private ChallengeRepository challengeRepository = mock(ChallengeRepository.class);
    private KeywordRepository keywordRepository = mock(KeywordRepository.class);
    private HashTagRepository hashTagRepository = mock(HashTagRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    private final Challenge CHALLENGE = Challenge.builder()
            .id(1l)
            .title("1일 1알고리즘")
            .description("하루에 알고리즘 한문제 이상 풀기")
            .vigibility(true)
            .authenticationType(AuthenticationType.PICTURE)
            .user(new User(1L, "홍길동", "1a2s@naver.com", "asdfasdfasdfas", "123124", UserRole.USER))
            .build();

    @BeforeEach
    void setup() {
        challengeService = new ChallengeService(
                challengeRepository, keywordRepository,
                hashTagRepository, userRepository);
    }

    @Test
    @DisplayName("챌린지 상세 조회 성공")
    void findByChallengeId_success() {
        Mockito.when(challengeRepository.findById(CHALLENGE.getId())).thenReturn(Optional.of(CHALLENGE));

        ChallengeDto challengeDto = challengeService.findByChallengeId(CHALLENGE.getId());

        assertEquals(challengeDto.getTitle(), CHALLENGE.getTitle());
        assertEquals(challengeDto.getAuthenticationType(), CHALLENGE.getAuthenticationType());
    }

    @Test
    @DisplayName("챌린지 상세 조회 실패 - 챌린지 존재하지 않을 때")
    void findByChallengeId_fail1() {
        Mockito.when(challengeRepository.findById(CHALLENGE.getId())).thenThrow(new ChallengeNotFoundException());

        assertThrows(ChallengeNotFoundException.class, () -> challengeService.findByChallengeId(CHALLENGE.getId()));
    }

    @Test
    @DisplayName("챌린지 상세 조회 실패 - 비공개 챌린지 접근할 때 ")
    void findByChallengeId_fail2() {
        Mockito.when(challengeRepository.findById(CHALLENGE.getId())).thenThrow(new InaccessibleChallengeException());

        assertThrows(InaccessibleChallengeException.class, () -> challengeService.findByChallengeId(CHALLENGE.getId()));
    }
}