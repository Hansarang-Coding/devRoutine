package com.likelion.devroutine.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.devroutine.challenge.dto.ChallengeResponse;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.challenge.service.ChallengeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChallengeController.class)
class ChallengeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ChallengeService challengeService;

    @Test
    @DisplayName("챌린지 상세 조회 성공")
    void findByChallengeId_success() throws Exception {
        ChallengeResponse challengeResponse=ChallengeResponse.builder()
                .id(1l)
                .title("1일 1 알고리즘 풀기")
                .description("하루에 알고리즘 하나 이상 풀기")
                .vigibility("공개")
                .authenticationType(AuthenticationType.PICTURE)
                .fromUserId(1l)
                .build();
        given(challengeService.findByChallengeId(any())).willReturn(challengeResponse);

        mockMvc.perform(get("/api/v1/challenges/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.authenticationType").exists());
    }
    @Test
    @DisplayName("챌린지 전체 리스트 - 조회 성공 ")
    void findAllchallenge_success() throws Exception {
        List<ChallengeResponse> challengeResponses=List.of(
                new ChallengeResponse(1l, "1일 1 알고리즘", "description", AuthenticationType.PICTURE, "공개", 1L, null),
                new ChallengeResponse(2l, "알고리즘 문제 풀기", "description", AuthenticationType.PICTURE, "비공개", 1L, null)
        );
        given(challengeService.findAllChallenge(null, 10)).willReturn(challengeResponses);

        mockMvc.perform(get("/api/v1/challenges")
                        .param("keyword", "알고리즘")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("챌린지 검색 - 조회 성공 ")
    void findSearchChallenge_success() throws Exception {
        List<ChallengeResponse> challengeResponses=List.of(
            new ChallengeResponse(1l, "1일 1 알고리즘", "description", AuthenticationType.PICTURE, "공개", 1L, null),
                new ChallengeResponse(2l, "알고리즘 문제 풀기", "description", AuthenticationType.PICTURE, "비공개", 1L, null)
        );
        given(challengeService.findAllChallengeTitle(null, 10, "알고리즘")).willReturn(challengeResponses);

        mockMvc.perform(get("/api/v1/challenges")
                .param("keyword", "알고리즘")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }
}