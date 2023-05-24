package com.likelion.devroutine.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.devroutine.auth.dto.SessionUser;
import com.likelion.devroutine.challenge.dto.*;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.challenge.service.ChallengeService;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.domain.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChallengeRestControllerTest {

    @Mock
    private ChallengeService challengeService;

    private ChallengeRestController challengeRestController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    private final ChallengeCreateRequest CHALLENGE_CREATE_REQUEST=new ChallengeCreateRequest("title", "description", true, AuthenticationType.PICTURE, null, LocalDate.now(), LocalDate.of(2024, 5, 31));
    private final ChallengeCreateResponse CHALLENGE_CREATEP_REPONSE=ChallengeCreateResponse.builder()
            .challengeId(1L)
            .title("title")
            .description("description")
            .authenticationType(AuthenticationType.PICTURE)
            .vigibility("공개")
            .build();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        challengeRestController=new ChallengeRestController(challengeService);
    }

    @Test
    @DisplayName("챌린지 생성 성공")
    void testCreateChallenge() throws Exception {
        Authentication authentication = mock(Authentication.class);
        ChallengeCreateRequest dto = new ChallengeCreateRequest();
        ChallengeCreateResponse expectedResponse = new ChallengeCreateResponse();

        when(challengeService.createChallenge(authentication.getName(), dto)).thenReturn(expectedResponse);

        ResponseEntity<ChallengeCreateResponse> response = challengeRestController.createChallenge(authentication, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(challengeService, times(1)).createChallenge(authentication.getName(), dto);
    }

    @Test
    @DisplayName("챌린지 검색 성공-keyword 없는 경우")
    void testFindAllChallengeList_WithoutKeyword() {
        String keyword = null;
        List<ChallengeDto> expectedChallenges = new ArrayList<>();
        when(challengeService.findAllChallenge()).thenReturn(expectedChallenges);

        ResponseEntity<List<ChallengeDto>> response = challengeRestController.findAllChallengeList(keyword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedChallenges, response.getBody());
        verify(challengeService, times(1)).findAllChallenge();
    }

    @Test
    @DisplayName("챌린지 검색 성공 - keyword 있는 경우")
    void testFindAllChallengeList_WithKeyword() {
        String keyword = "example";
        List<ChallengeDto> expectedChallenges = new ArrayList<>();
        when(challengeService.findAllChallengeTitle(keyword)).thenReturn(expectedChallenges);

        ResponseEntity<List<ChallengeDto>> response = challengeRestController.findAllChallengeList(keyword);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedChallenges, response.getBody());
        verify(challengeService, times(1)).findAllChallengeTitle(keyword);
    }

    @Test
    @DisplayName("챌린지 상세 조회 - 참여중이지 않은 경우")
    void testFindByChallengeId_NotParticipating() {
        Authentication authentication = mock(Authentication.class);
        Long id = 1L;
        SessionUser sessionUser = null;
        ChallengeDto expectedChallengeDto = new ChallengeDto();

        when(challengeService.isParticipate(id, authentication.getName())).thenReturn(false);
        when(challengeService.findByChallengeId(id)).thenReturn(expectedChallengeDto);

        ResponseEntity<ChallengeDto> response = challengeRestController.findByChallengeId(authentication, id, sessionUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedChallengeDto, response.getBody());
        verify(challengeService, times(1)).findByChallengeId(id);
        verify(challengeService, never()).findByChallengeId(id, authentication.getName());
    }

    /*@Test
    @DisplayName("챌린지 상세 조회 성공 - 참여중인 경우")
    void testFindByChallengeId_Participating() {
        Authentication authentication = mock(Authentication.class);
        Long id = 1L;
        SessionUser sessionUser = new SessionUser(new User(1L, "testname", "testEmail", null, "test_oauth", UserRole.USER));
        ChallengeDto expectedChallengeDto = new ChallengeDto();
        HttpHeaders expectedHeaders = new HttpHeaders();

        when(challengeService.isParticipate(id, authentication.getName())).thenReturn(true);
        when(challengeService.findByChallengeId(id, authentication.getName())).thenReturn(expectedChallengeDto);

        ResponseEntity<ChallengeDto> response = challengeRestController.findByChallengeId(authentication, id, sessionUser);

        System.out.println(response.getBody());
        assertEquals(HttpStatus.MOVED_PERMANENTLY, response.getStatusCode());
        assertEquals(expectedChallengeDto, response.getBody());
        assertEquals(expectedHeaders.getLocation(), response.getHeaders().getLocation());
        verify(challengeService, never()).findByChallengeId(id);
        verify(challengeService, times(1)).findByChallengeId(id, authentication.getName());
    }*/

    @Test
    @DisplayName("챌린지 삭제 성공")
    void testDeleteChallenge() {
        Authentication authentication = mock(Authentication.class);
        Long id = 1L;
        ChallengeResponse expectedResponse = new ChallengeResponse(id, "챌린지 삭제 성공");

        when(challengeService.deleteChallenge(authentication.getName(), id)).thenReturn(expectedResponse);

        ResponseEntity<ChallengeResponse> response = challengeRestController.deleteChallenge(authentication, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(challengeService, times(1)).deleteChallenge(authentication.getName(), id);
    }

    @Test
    @DisplayName("챌린지 수정 성공")
    void testModifyChallenge() {
        Authentication authentication = mock(Authentication.class);
        Long id = 1L;
        ChallengeModifiyRequest dto = new ChallengeModifiyRequest();
        ChallengeResponse expectedResponse = new ChallengeResponse(id, "챌린지 수정 성공");

        when(challengeService.modifyChallenge(authentication.getName(), id, dto)).thenReturn(expectedResponse);

        ResponseEntity<ChallengeResponse> response = challengeRestController.modifyChallenge(authentication, id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
        verify(challengeService, times(1)).modifyChallenge(authentication.getName(), id, dto);
    }

}
