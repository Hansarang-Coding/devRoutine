package com.likelion.devroutine.certification.util;

import com.likelion.devroutine.certification.dto.github.UserEventDto;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.likelion.devroutine.certification.enumerate.CrawlingUrl.BASE_URL;

@Slf4j
@Component
public class GithubCrawl {
    public List<UserEventDto> getUserEvents(String userName){
        //pushㄱㅏ 오늘 내에 있는 리포지토리 받아오기
        //해당 리포지토리 별로 오늘 커밋내역 가져오기..?
        List<UserEventDto> events=WebClient.create(BASE_URL.getUrl())
                .get()
                .uri(getUserEventUrl(userName))
                .retrieve()
                .bodyToFlux(UserEventDto.class)
                .filter(res->
                       res.getType().equals("PushEvent") && res.getCreated_at().plusHours(57l).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(LocalDate.now().toString()))
                .collect(Collectors.toList())
                .block();

        return events;
    }

    private String getUserEventUrl(String userName) {
        return "/users/"+userName+"/events";
    }
}