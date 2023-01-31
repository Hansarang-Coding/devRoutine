package com.likelion.devroutine.alarm.controller;

import com.likelion.devroutine.alarm.dto.AlarmResponse;
import com.likelion.devroutine.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
//
//    @GetMapping(value = "/", produces = "text/event-stream")
//    public ResponseEntity<SseEmitter> subscribe(Authentication authentication,
//                                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//        return ResponseEntity.ok().body(alarmService.subscribe(authentication.getName(), lastEventId));
//    }

    @GetMapping
    public ResponseEntity<Page<AlarmResponse>> findAlarms(@PageableDefault(size=20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok().body(alarmService.findAlarms(pageable));
    }

}
