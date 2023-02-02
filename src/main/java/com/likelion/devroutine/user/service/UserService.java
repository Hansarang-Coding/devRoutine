package com.likelion.devroutine.user.service;

import com.likelion.devroutine.alarm.domain.Alarm;
import com.likelion.devroutine.alarm.dto.AlarmResponse;
import com.likelion.devroutine.alarm.enumurate.AlarmType;
import com.likelion.devroutine.alarm.repository.AlarmRepository;
import com.likelion.devroutine.alarm.repository.EmitterRepository;
import com.likelion.devroutine.follow.domain.Follow;
import com.likelion.devroutine.follow.dto.FollowCreateResponse;
import com.likelion.devroutine.follow.dto.FollowingResponse;
import com.likelion.devroutine.follow.exception.AlreadyFollowingException;
import com.likelion.devroutine.follow.exception.FollowNotPermittedException;
import com.likelion.devroutine.follow.exception.FollowingNotFoundException;
import com.likelion.devroutine.follow.repository.FollowRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private final AlarmRepository alarmRepository;
    //private final EmitterRepository emitterRepository;

    @Transactional
    public FollowCreateResponse follow(Long followerId, String oauthId) {
        User followingUser = findUserByOauthId(oauthId);
        User follower = findUser(followerId);
        validateFollowExists(follower.getId(), followingUser.getId());
        validateSelfFollow(follower.getId(), followingUser.getId());
        Follow follow = Follow.createFollow(follower, followingUser);
        followRepository.save(follow);

        //알람
        followAlarm(follower,followingUser.getId());
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

    public List<FollowingResponse> findFollowers(Long userId) {
        validateUserExists(userId);
        List<Follow> followers = followRepository.findByFollowerId(userId);
        return FollowingResponse.of(followers);
    }

    //알림 기능
    private void followAlarm(User followerUser, Long followingUser) {
        Alarm savedAlarm = alarmRepository.save(Alarm.createAlarm(followingUser,
                AlarmType.NEW_FOLLOW, AlarmType.NEW_FOLLOW.getMessage(), followerUser));

//        String receiverId = String.valueOf(followerUser.getId());
//        String eventId = receiverId + "_" + System.currentTimeMillis();
//
//        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByUserId(receiverId);
//        emitters.forEach(
//                (key, emitter) -> {
//                    emitterRepository.saveEventCache(key, savedAlarm);
//                    //sendNotification(emitter, eventId, key, AlarmResponse.toList());
//                }
//        );

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



//    private void send( receiver, AlarmType alarmTypeType, String content, String url) {
//
//
//        Notification notification = notificationRepository.save(createNotification(receiver, notificationType, content, url));
//
//        String receiverId = String.valueOf(receiver.getId());
//        String eventId = receiverId + "_" + System.currentTimeMillis();
//        Map<String, SseEmitter> emitters = emitterRepository.findAllEmitterStartWithByMemberId(receiverId);
//        emitters.forEach(
//                (key, emitter) -> {
//                    emitterRepository.saveEventCache(key, notification);
//                    sendNotification(emitter, eventId, key, NotificationResponseDto.create(notification));
//                }
//        );
//    }



}
