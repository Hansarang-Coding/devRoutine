package com.likelion.devroutine.certification.dto.github;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Setter
@ToString
@NoArgsConstructor
public class PayloadDto {
    private ArrayList<CommitDto> commits;
}
