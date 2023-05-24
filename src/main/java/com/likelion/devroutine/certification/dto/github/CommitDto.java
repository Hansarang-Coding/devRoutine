package com.likelion.devroutine.certification.dto.github;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Setter
@ToString
@NoArgsConstructor
public class CommitDto {
    private String message;
    private GitUserDto author;
}
