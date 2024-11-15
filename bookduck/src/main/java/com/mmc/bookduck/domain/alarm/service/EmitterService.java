package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import com.mmc.bookduck.domain.alarm.repository.AlarmRepository;
import com.mmc.bookduck.domain.alarm.repository.EmitterRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmitterService {
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final UserService userService;

    private static final Long DEFAULT_TIMEOUT = 5L * 60 * 1000;  // 5분

    public SseEmitter subscribe() {
        User user = userService.getCurrentUser();
        Long userId = user.getUserId();

        // 이미 존재하는 Emitter가 있는지 확인
        SseEmitter emitter = Optional.ofNullable(emitterRepository.get(userId))
                .orElseGet(() -> registerEmitter(userId));

        sendToClientIfNewAlarmExists(user);
        return emitter;
    }

    private void sendToClientIfNewAlarmExists(User user) {
        Boolean isMissedAlarms = alarmRepository.existsByReceiverAndIsReadFalse(user);
        if (isMissedAlarms.equals(true)) {
            sendToClient(user.getUserId(), AlarmDefaultDataDto.from(true), "new sse alarm exists");
        } else {
            sendToClient(user.getUserId(), AlarmDefaultDataDto.from(false), "new sse alarm doesn't exists");
        }
    }

    private SseEmitter registerEmitter(Long memberId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(memberId, emitter);

        emitter.onCompletion(() -> emitterRepository.delete(memberId));
        emitter.onTimeout(() -> emitterRepository.delete(memberId));

        return emitter;
    }

    public void notify(Long memberId, Object data, String comment) {
        sendToClient(memberId, data, comment);
    }

    private <T> void sendToClient(Long memberId, Object data, String comment) {
        SseEmitter emitter = emitterRepository.get(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(memberId))
                        .name("sse-alarm")
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.delete(memberId);
                emitter.completeWithError(e);
            }
        }
    }
}
