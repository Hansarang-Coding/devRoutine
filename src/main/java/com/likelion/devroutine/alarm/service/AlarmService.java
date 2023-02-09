package com.likelion.devroutine.alarm.service;

import com.likelion.devroutine.alarm.domain.Alarm;
import com.likelion.devroutine.alarm.dto.AlarmResponse;
import com.likelion.devroutine.alarm.repository.AlarmRepository;
import com.likelion.devroutine.alarm.repository.EmitterRepository;
import com.likelion.devroutine.user.domain.User;
import com.likelion.devroutine.user.exception.UserNotFoundException;
import com.likelion.devroutine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    private final EmitterRepository emitterRepository;

    private static final Long timeout = 1000 * 60L;

    public List<AlarmResponse> findAlarms(String oauthId) {
        User alarmUser= findUserByOauthId(oauthId);
        List<Alarm> alarms = alarmRepository.findByUserId(alarmUser.getId());
        return AlarmResponse.of(alarms);
    }

    private User findUserByOauthId(String OauthId) {
        return userRepository.findByOauthId(OauthId)
                .orElseThrow(UserNotFoundException::new);
    }



    


    public SseEmitter subscribe(String oauthId, String lastEventId) {
        User user = findUserByOauthId(oauthId);
        String emitterId = makeTimeIncludeId(user.getId());
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(timeout));
        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        // 503 에러를 방지하기 위한 더미 이벤트 전송
        String eventId = makeTimeIncludeId(user.getId());
        sendNotification(emitter, eventId, emitterId, "EventStream Created. [userId=" + user.getId() + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (hasLostData(lastEventId)) {
            sendLostData(lastEventId, user.getId(), emitterId, emitter);
        }

        return emitter;
    }

    private String makeTimeIncludeId(Long userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private void sendNotification(SseEmitter emitter, String eventId, String emitterId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
        }
    }

    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(String lastEventId, Long memberId, String emitterId, SseEmitter emitter) {
        Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(memberId));
        eventCaches.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendNotification(emitter, entry.getKey(), emitterId, entry.getValue()));
    }




}
