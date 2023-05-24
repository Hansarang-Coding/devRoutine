package com.likelion.devroutine.certification.util;

import com.likelion.devroutine.certification.dto.github.RepositoryDto;
import com.likelion.devroutine.certification.dto.github.CommitApiDto;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.likelion.devroutine.certification.enumerate.CrawlingUrl.BASE_URL;

@Slf4j
@Component
public class GithubCrawl {
    public List<RepositoryDto> getUserRepository(String userName){
        List<RepositoryDto> repos=WebClient.create(BASE_URL.getUrl())
                .get()
                .uri(getUserRepositoryUrl(userName))
                .retrieve()
                .bodyToFlux(RepositoryDto.class)
                .filter(repositoryDto ->
                        repositoryDto.getPushed_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(LocalDate.now())
                )
                .sort(Comparator.comparing(RepositoryDto::getPushed_at).reversed())
                .collect(Collectors.toList())
                .block();

        log.info(repos.get(0).getName());
        return repos;
    }
    public List<CommitApiDto> getUserCommit(String repo, String userName){
        List<CommitApiDto> commits=WebClient.create(BASE_URL.getUrl())
                .get()
                .uri(getRepositoryCommitUrl(userName, repo))
                .retrieve()
                .bodyToFlux(CommitApiDto.class)
                .filter(response ->
                        response.getCommit().getAuthor().getName().equals(userName)
                )
                .collect(Collectors.toList())
                .block();

        //log.info(commits.get(0).getCommit().getMessage());

        return commits;
    }
    public String getUserRepositoryUrl(String userName){
        return "/users/"+userName+"/repos";
    }

    public String getRepositoryCommitUrl(String userName, String repoName){
        return "/repos/"+userName+
                "/"+repoName+"/commits";
    }
}