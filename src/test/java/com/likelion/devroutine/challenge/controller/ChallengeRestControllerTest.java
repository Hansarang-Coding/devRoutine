package com.likelion.devroutine.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.devroutine.challenge.dto.ChallengeCreateRequest;
import com.likelion.devroutine.challenge.dto.ChallengeCreateResponse;
import com.likelion.devroutine.challenge.dto.ChallengeDto;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.challenge.service.ChallengeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
//import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChallengeRestController.class)
class ChallengeRestControllerTest {
    @Autowired
    WebApplicationContext context; // MockMvc 객체 생성을 위한 context
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ChallengeService challengeService;
    @MockBean
    RedisConnectionFactory redisConnectionFactory;

    MockHttpSession session;

    private final ChallengeCreateRequest challengeCreateRequest =
            new ChallengeCreateRequest("title", "description", true, null, "#test", null, null);

    @BeforeEach
    void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build(); //test를 위한 MockMvc 객체 생성. 스프링이 로드한 WebApplicationContext의 인스턴스로 작동
        session = new MockHttpSession();
        session.setAttribute("name", "test");
    }

    @AfterEach
    void clean(){
        session.clearAttributes();
    }

    @Test
    @DisplayName("챌린지 상세 조회 성공")
    void findByChallengeId_success() throws Exception {
        ChallengeDto challengeDto= ChallengeDto.builder()
                .id(1l)
                .title("1일 1 알고리즘 풀기")
                .description("하루에 알고리즘 하나 이상 풀기")
                .vigibility("공개")
                .authenticationType(AuthenticationType.PICTURE)
                .fromUserId(1l)
                .build();
        given(challengeService.findByChallengeId(any())).willReturn(challengeDto);

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
        List<ChallengeDto> challengeDtos=List.of(
                new ChallengeDto(1l, "1일 1 알고리즘", "description", AuthenticationType.PICTURE, "공개", 1L, null),
                new ChallengeDto(2l, "알고리즘 문제 풀기", "description", AuthenticationType.PICTURE, "비공개", 1L, null)
        );

        given(challengeService.findAllChallenge(null, 10)).willReturn(challengeDtos);

        mockMvc.perform(get("/api/v1/challenges")
                        .param("keyword", "알고리즘")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("챌린지 검색 - 조회 성공 ")
    void findSearchChallenge_success() throws Exception {
        List<ChallengeDto> challengeDtos=List.of(
            new ChallengeDto(1l, "1일 1 알고리즘", "description", AuthenticationType.PICTURE, "공개", 1L, null),
                new ChallengeDto(2l, "알고리즘 문제 풀기", "description", AuthenticationType.PICTURE, "비공개", 1L, null)
        );
        given(challengeService.findAllChallengeTitle(null, 10, "알고리즘")).willReturn(challengeDtos);

        mockMvc.perform(get("/api/v1/challenges")
                .param("keyword", "알고리즘")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    /*@Test
    @DisplayName("챌린지 생성 성공")
    void createChallenge_success() throws Exception {
        ChallengeCreateResponse challengeCreateResponse=ChallengeCreateResponse.builder()
                .challengeId(1l)
                .title("title")
                .description("description")
                .build();
        given(challengeService.createChallenge(any(), any())).willReturn(challengeCreateResponse);
        mockMvc.perform(post("/api/v1/challenges")
                        .with(csrf())
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(challengeCreateRequest)))
                .andDo(print())
                .andExpect(status().isOk());

    }*/
}