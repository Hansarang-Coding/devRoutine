package com.likelion.devroutine.user.service;

import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.dto.FollowCreateResponse;
import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.dto.FollowingResponse;
import com.likelion.devroutine.follow.exception.AlreadyFollowingException;
import com.likelion.devroutine.follow.exception.FollowNotPermittedException;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.participant.domain.Participation;
import com.likelion.devroutine.participant.repository.ParticipationRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.dto.MyProfileResponse;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    @Transactional
    public FollowCreateResponse follow(Long followerId, String oauthId) {
        User followingUser = findUserByOauthId(oauthId);
        User follower = findUser(followerId);
        validateFollowExists(follower.getId(), followingUser.getId());
        validateSelfFollow(follower.getId(), followingUser.getId());
        Follow follow = Follow.createFollow(follower, followingUser);
        followRepository.save(follow);
        return FollowCreateResponse.of(followingUser.getName(), follower.getName());
    }

    private void validateSelfFollow(Long followerId, Long followingUserId) {
        if (Objects.equals(followerId, followingUserId)) {
            throw new FollowNotPermittedException();
        }
    }

    private void validateFollowExists(Long followerId, Long followingUserId) {
        if (followRepository.existsByFollowerIdAndFollowingId(followerId, followingUserId)) {
            throw new AlreadyFollowingException();
        }
    }

    @Transactional
    public void unfollow(Long followerId, String userName) {
        User followingUser = findUserByOauthId(userName);
        User follower = findUser(followerId);
        Follow follow = findExistingFollow(follower.getId(), followingUser.getId());
        followRepository.delete(follow);
    }

    public List<FollowingResponse> findFollowings(Long userId) {
        validateUserExists(userId);
        List<Follow> followings = followRepository.findByFollowingId(userId);
        return FollowingResponse.of(followings);
    }

    public List<FollowerResponse> findFollowers(Long userId) {
        validateUserExists(userId);
        List<Follow> followers = followRepository.findByFollowerId(userId);
        return FollowerResponse.of(followers);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    private Follow findExistingFollow(Long followerId, Long followingUserId) {
        return followRepository.findByFollowerIdAndFollowingId(followerId, followingUserId)
                .orElseThrow(FollowingNotFoundException::new);
    }

    private User findUserByOauthId(String OauthId) {
        return userRepository.findByOauthId(OauthId)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException();
        }
    }

    public MyProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<Participation> participations = findParticipations(user.getId());
        Long followers = followRepository.countFollowers(user.getId());
        Long following = followRepository.countFollowing(user.getId());
        return MyProfileResponse.of(user, followers, following, participations);
    }

    private List<Participation> findParticipations(Long userId) {
        return participationRepository.findAllByUserId(userId);
    }



}
