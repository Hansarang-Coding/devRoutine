package com.likelion.devroutine.user.controller;

import com.likelion.devroutine.follow.dto.FollowCreateResponse;
import com.likelion.devroutine.follow.dto.FollowerResponse;
import com.likelion.devroutine.follow.dto.FollowingResponse;
import com.likelion.devroutine.user.dto.MyProfileResponse;
import com.likelion.devroutine.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserRestController {

    private final UserService userService;

    @PostMapping("{userId}/follow")
    public ResponseEntity<FollowCreateResponse> follow(@PathVariable("userId") Long userId,
                                                       Authentication authentication) {
        log.info("authentication.getName() : {}", authentication.getName());
        return ResponseEntity.ok().body(userService.follow(userId, authentication.getName()));
    }

    @PostMapping("{userId}/unfollow")
    public ResponseEntity<Void> unfollow(@PathVariable("userId") Long userId,
                                         Authentication authentication) {
        userService.unfollow(userId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{userId}/following")
    public ResponseEntity<List<FollowingResponse>> findfollowing(@PathVariable("userId") Long userId) {
        List<FollowingResponse> following = userService.findFollowings(userId);
        return ResponseEntity.ok().body(following);
    }

    @GetMapping("{userId}/followers")
    public ResponseEntity<List<FollowerResponse>> findFollowers(@PathVariable("userId") Long userId) {
        List<FollowerResponse> followers = userService.findFollowers(userId);
        return ResponseEntity.ok().body(followers);
    }

    @GetMapping("/profile")
    public ResponseEntity<MyProfileResponse> getProfile(Authentication authentication) {
        MyProfileResponse profile = userService.getProfile(authentication.getName());
        return ResponseEntity.ok().body(profile);
    }
}