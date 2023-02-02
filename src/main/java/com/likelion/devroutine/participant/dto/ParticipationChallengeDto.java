package com.likelion.devroutine.participant.dto;

import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.hashtag.dto.ChallengeHashTagResponse;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationChallengeDto {
    private Long id;
    private String title;
    private String description;
    private String vigibility;
    private AuthenticationType authenticationType;
    private List<UserResponse> participants;
    private List<ChallengeHashTagResponse> challengeHashTag;

   public static ParticipationChallengeDto toResponse(Participation participation, List<ChallengeHashTagResponse> challengeHashTags, List<UserResponse> participants) {
        return ParticipationChallengeDto.builder()
                .id(participation.getId())
                .title(participation.getChallenge().getTitle())
                .description(participation.getChallenge().getDescription())
                .authenticationType(participation.getChallenge().getAuthenticationType())
                .vigibility(participation.getChallenge().getVigibility()? "공개" : "비공개")
                .participants(participants)
                .challengeHashTag(challengeHashTags)
                .build();
    }
}
