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
    WebApplicationContext context; // MockMvc ?????? ????????? ?????? context
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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build(); //test??? ?????? MockMvc ?????? ??????. ???????????? ????????? WebApplicationContext??? ??????????????? ??????
        session = new MockHttpSession();
        session.setAttribute("name", "test");
    }

    @AfterEach
    void clean(){
        session.clearAttributes();
    }

    @Test
    @DisplayName("????????? ?????? ?????? ??????")
    void findByChallengeId_success() throws Exception {
        ChallengeDto challengeDto= ChallengeDto.builder()
                .id(1l)
                .title("1??? 1 ???????????? ??????")
                .description("????????? ???????????? ?????? ?????? ??????")
                .vigibility("??????")
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
    @DisplayName("????????? ?????? ????????? - ?????? ?????? ")
    void findAllchallenge_success() throws Exception {
        List<ChallengeDto> challengeDtos=List.of(
                new ChallengeDto(1l, "1??? 1 ????????????", "description", AuthenticationType.PICTURE, "??????", 1L, null),
                new ChallengeDto(2l, "???????????? ?????? ??????", "description", AuthenticationType.PICTURE, "?????????", 1L, null)
        );

        given(challengeService.findAllChallenge(null, 10)).willReturn(challengeDtos);

        mockMvc.perform(get("/api/v1/challenges")
                        .param("keyword", "????????????")
                        .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @DisplayName("????????? ?????? - ?????? ?????? ")
    void findSearchChallenge_success() throws Exception {
        List<ChallengeDto> challengeDtos=List.of(
            new ChallengeDto(1l, "1??? 1 ????????????", "description", AuthenticationType.PICTURE, "??????", 1L, null),
                new ChallengeDto(2l, "???????????? ?????? ??????", "description", AuthenticationType.PICTURE, "?????????", 1L, null)
        );
        given(challengeService.findAllChallengeTitle(null, 10, "????????????")).willReturn(challengeDtos);

        mockMvc.perform(get("/api/v1/challenges")
                .param("keyword", "????????????")
                .param("size", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    /*@Test
    @DisplayName("????????? ?????? ??????")
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