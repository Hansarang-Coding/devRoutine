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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmRestController {

    private final AlarmService alarmService;

//    @GetMapping(value = "/connect", produces = "text/event-stream")
//    @ResponseStatus(HttpStatus.OK)
//    public SseEmitter subscribe(Authentication authentication,
//                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//        return alarmService.subscribe(authentication.getName(), lastEventId);
//    }


    @GetMapping
    public ResponseEntity<List<AlarmResponse>> findAlarms(Authentication authentication) {
        return ResponseEntity.ok().body(alarmService.findAlarms(authentication.getName()));
                //notificationService.subscribe(memberDetails.getId(), lastEventId);
    }

}
