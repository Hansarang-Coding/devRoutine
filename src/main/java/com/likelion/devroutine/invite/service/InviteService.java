package com.likelion.devroutine.invite.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.invite.dto.*;
import com.likelion.devroutine.invite.enumerate.ResponseMessage;
import com.likelion.devroutine.invite.exception.BadRequestInviteAcceptException;
import com.likelion.devroutine.invite.exception.InviteNotFoundException;
import com.likelion.devroutine.invite.repository.InviteRepository;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.exception.DuplicatedParticipationException;
import com.likelion.devroutine.participant.repository.ParticipationRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class InviteService {
    private final InviteRepository inviteRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final FollowRepository followRepository;
    private final ParticipationRepository participationRepository;

    public InviteService(InviteRepository inviteRepository, UserRepository userRepository, ChallengeRepository challengeRepository, FollowRepository followRepository, ParticipationRepository participationRepository) {
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.followRepository = followRepository;
        this.participationRepository = participationRepository;
    }

    @Transactional
    public InviteResponse acceptInvite(String oauthId, Long inviteId) {
        User user=getUser(oauthId);
        Invite invite=getInvite(inviteId);
        validateInvitee(invite, user);
        Challenge challenge=getChallenge(invite.getChallengeId());
        validateParticipate(user, challenge);
        Participation savedParticipation=participationRepository.save(Participation.createParticipant(user, challenge));
        invite.deleteInvite();
        return InviteResponse.builder()
                .challengeId(savedParticipation.getChallenge().getId())
                .message(ResponseMessage.INVITE_ACCEPT.getMessage())
                .build();
    }

    public InviteReadResponse findAllInvite(String oauthId) {
        User user=getUser(oauthId);
        List<InviteeResponse> inviteeResponses=inviteRepository.findInviteeByInviterId(user.getId());
        List<InviterResponse> inviterResponses=inviteRepository.findInviterByInviteeId(user.getId());
        return InviteReadResponse.builder()
                .inviteeResponses(inviteeResponses)
                .inviterResponse(inviterResponses)
                .build();
    }
    @Transactional
    public InviteCancelResponse rejectInvite(String oauthId, Long inviteId) {
        User user=getUser(oauthId);
        Invite invite=inviteRepository.findById(inviteId)
                .orElseThrow(InviteNotFoundException::new);

        invite.deleteInvite();
        return InviteCancelResponse.builder()
                .message(ResponseMessage.INVITE_REJECT.getMessage())
                .build();
    }

    @Transactional
    public InviteCancelResponse cancelInvite(String oauthId, Long inviteId) {
        User user=getUser(oauthId);
        Invite invite=getInvite(inviteId);
        invite.deleteInvite();
        return InviteCancelResponse.builder()
                .message(ResponseMessage.INVITE_CANCEL.getMessage())
                .build();
    }

    private boolean validateInvitee(Invite invite, User user) {
        if(invite.getInviteeId().equals(user.getId())){
            return true;
        }
        throw new BadRequestInviteAcceptException();
    }

    private boolean matchWriterAndUser(User user, Challenge challenge) {
        if(!user.getId().equals(challenge.getUserId())){
            throw new InvalidPermissionException();
        }
        return true;
    }

    private Challenge getChallenge(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(()->new ChallengeNotFoundException());
    }

    public User getUser(String oauthId){
        return userRepository.findByOauthId(oauthId)
                .orElseThrow(()->new UserNotFoundException());
    }

    public Invite getInvite(Long inviteId){
        return inviteRepository.findById(inviteId)
                .orElseThrow(()-> new InviteNotFoundException());
    }
    public boolean isProgressChallenge(LocalDate startDate) {
        if (LocalDate.now().isBefore(startDate))
            return true;
        throw new InProgressingChallengeException();
    }
    public boolean validateParticipate(User user, Challenge challenge){
        isProgressChallenge(challenge.getStartDate());
        validateDuplicateParticipate(user, challenge);
        return true;
    }
    public boolean validateDuplicateParticipate(User user, Challenge challenge){
        participationRepository.findByUserAndChallenge(user, challenge)
                .ifPresent(participant->{
                    throw new DuplicatedParticipationException();
                });
        return true;
    }
}
