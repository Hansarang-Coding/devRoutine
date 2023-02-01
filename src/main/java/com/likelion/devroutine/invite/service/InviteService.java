package com.likelion.devroutine.invite.service;

import com.likelion.devroutine.challenge.domain.Challenge;
import com.likelion.devroutine.challenge.exception.ChallengeNotFoundException;
import com.likelion.devroutine.challenge.exception.InProgressingChallengeException;
import com.likelion.devroutine.challenge.exception.InvalidPermissionException;
import com.likelion.devroutine.challenge.repository.ChallengeRepository;
import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.dto.FollowingResponse;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.invite.domain.Invite;
import com.likelion.devroutine.invite.dto.InviteAcceptResponse;
import com.likelion.devroutine.invite.dto.InviteResponse;
import com.likelion.devroutine.invite.enumerate.ResponseMessage;
import com.likelion.devroutine.invite.repository.InviteRepository;
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

    public InviteService(InviteRepository inviteRepository, UserRepository userRepository, ChallengeRepository challengeRepository, FollowRepository followRepository) {
        this.inviteRepository = inviteRepository;
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.followRepository = followRepository;
    }

    public List<FollowerResponse> getFollowList(String oauthId, Long challengeId) {
        User user = getUser(oauthId);
        Challenge challenge=getChallenge(challengeId);
        matchWriterAndUser(user, challenge);
        List<Follow> followers=followRepository.findByFollowerId(user.getId());
        return FollowerResponse.of(followers);
    }
    @Transactional
    public InviteResponse inviteUser(String inviterOauthId, Long challengeId, Long inviteeId) {
        User inviter=getUser(inviterOauthId);
        User invitee=userRepository.findById(inviteeId)
                .orElseThrow(()->new UserNotFoundException());
        validateFollower(inviter, invitee);
        Challenge challenge=getChallenge(challengeId);
        matchWriterAndUser(inviter, challenge);
        isProgressChallenge(challenge.getStartDate());
        Invite savedInvite=inviteRepository.save(Invite.createInvite(challenge.getId(), inviter.getId(), invitee.getId()));
        return InviteResponse.builder()
                .inviterId(savedInvite.getInviterId())
                .challengeId(savedInvite.getChallengeId())
                .inviteeId(savedInvite.getInviteeId())
                .message(ResponseMessage.INVITE_SUCCESS.getMessage())
                .build();
    }

    //invitee가 inviter를 팔로우하고 있는지 확인하는 함수
    private void validateFollower(User inviter, User invitee) {
        Follow follow=followRepository.findByFollowerIdAndFollowingId(inviter.getId(), invitee.getId())
                .orElseThrow(()->new FollowingNotFoundException());
    }

    private boolean matchWriterAndUser(User user, Challenge challenge) {
        if(!user.getId().equals(challenge.getUser().getId())){
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
    public boolean isProgressChallenge(LocalDate startDate) {
        if (LocalDate.now().isAfter(startDate))
            throw new InProgressingChallengeException();
        return false;
    }
}
