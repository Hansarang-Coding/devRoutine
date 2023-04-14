package com.likelion.devroutine.certification.dto.github;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Setter
@ToString
@NoArgsConstructor
public class GitUserDto {
    private String name;
    private String email;
    private LocalDateTime date;
}
