package com.likelion.devroutine.participant.dto;

import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import com.likelion.devroutine.participant.domain.Participant;
import com.likelion.devroutine.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipateChallengeDto {
    private Long id;
    private String title;
    private String description;
    private String vigibility;
    private AuthenticationType authenticationType;
    private List<User> participants;
    private List<ChallengeHashTagResponse> challengeHashTag;

   public static ParticipateChallengeDto toResponse(Participant participant, List<ChallengeHashTagResponse> challengeHashTags, List<User> participants) {
        return ParticipateChallengeDto.builder()
                .id(participant.getId())
                .title(participant.getChallenge().getTitle())
                .description(participant.getChallenge().getDescription())
                .authenticationType(participant.getChallenge().getAuthenticationType())
                .vigibility(participant.getChallenge().getVigibility()? "공개" : "비공개")
                .participants(participants)
                .challengeHashTag(challengeHashTags)
                .build();
    }
}
