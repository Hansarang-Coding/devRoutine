package com.likelion.devroutine.certification.dto.github;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@ToString
@NoArgsConstructor
public class CommitApiDto {
    private String htmlUrl;
    private String sha;
    private CommitDto commit;
}
