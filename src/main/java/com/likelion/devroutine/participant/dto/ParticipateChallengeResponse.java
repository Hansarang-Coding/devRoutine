package com.likelion.devroutine.participant.dto;

import com.likelion.devroutine.auth.domain.User;
import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.enumerate.AuthenticationType;
import com.likelion.devroutine.participant.domain.Participant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipateChallengeResponse {
    private Long id;
    private String title;
    private String description;
    private String vigibility;
    private AuthenticationType authenticationType;
    private List<User> participants;

    public static ParticipateChallengeResponse toResponse(Participant participant, List<Participant> participants) {
        return ParticipateChallengeResponse.builder().build();
    }

/*    public static ParticipateChallengeResponse toResponse(UserChallenge userChallenge, List<User> participants) {
        return ParticipateChallengeResponse.builder()
                .id(userChallenge.getId())
                .title(userChallenge.getChallenge().getTitle())
                .description(userChallenge.getChallenge().getDescription())
                .authenticationType(userChallenge.getChallenge().getAuthenticationType())
                .participants(participants)
                .build();
    }*/
}
