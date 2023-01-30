package com.likelion.devroutine.user.service;

import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.dto.FollowCreateResponse;
import com.likelion.devroutine.follow.dto.FollowingResponse;
import com.likelion.devroutine.follow.exception.AlreadyFollowingException;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public FollowCreateResponse follow(Long followerId, String oauthId) {
        User followingUser = findUserByOauthId(oauthId);
        User follower = findUser(followerId);
        validateExistingFollow(follower.getId(), followingUser.getId());
        Follow follow = Follow.createFollow(follower, followingUser);
        followRepository.save(follow);
        return FollowCreateResponse.of(followingUser.getName(), follower.getName());
    }

    private void validateExistingFollow(Long followerId, Long followingUserId) {
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

    public List<FollowingResponse> findFollowers(Long userId) {
        validateUserExists(userId);
        List<Follow> followers = followRepository.findByFollowerId(userId);
        return FollowingResponse.of(followers);
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
}
