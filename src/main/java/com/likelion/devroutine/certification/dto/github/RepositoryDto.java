package com.likelion.devroutine.certification.dto.github;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@ToString
@NoArgsConstructor
public class RepositoryDto {
    private String name;
    private String url;
    private LocalDateTime pushed_at;
}
