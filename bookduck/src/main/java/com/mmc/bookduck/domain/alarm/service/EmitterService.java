package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.ssedata.AlarmBadgeUnlockedDataDto;
import com.mmc.bookduck.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;
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

    private static final Long DEFAULT_TIMEOUT = 3L * 60 * 1000;  // 3분

    public SseEmitter subscribe() {
        User user = userService.getCurrentUser();
        Long userId = user.getUserId();

        // 이미 존재하는 Emitter가 있는지 확인
        SseEmitter emitter = Optional.ofNullable(emitterRepository.get(userId))
                .orElseGet(() -> registerEmitter(userId));

        sendToClientIfNewAlarmExists(user);
        return emitter;
    }

    // 알림 상태를 확인하고 클라이언트에 알림 전송
    public void sendToClientIfNewAlarmExists(User user) {
        boolean isMissedAlarms = alarmRepository.existsByReceiverAndIsReadFalse(user);
        boolean isAnnouncementChecked = user.getIsAnnouncementChecked();
        boolean isItemUnlockedChecked = user.getIsItemUnlockedChecked();

        String message = (isMissedAlarms || isAnnouncementChecked || isItemUnlockedChecked)
                ? "new sse alarm exists"
                : "new sse alarm doesn't exists";
        
        // 아이템 잠금 해제를 확인한 것으로 저장
        if (isItemUnlockedChecked) {
            user.setIsItemUnlockedChecked(true);
            userService.saveUser(user);
        }
        
        sendToClient(user.getUserId(), AlarmDefaultDataDto.from(!isMissedAlarms, isAnnouncementChecked, isItemUnlockedChecked), message);
    }

    private void sendToClientIfBadgeUnlockedAlarmExists(User user) {
        Boolean isBadgeUnlocked = alarmRepository.existsByReceiverAndIsReadFalseAndAlarmType(user, AlarmType.BADGE_UNLOCKED);
        if (isBadgeUnlocked.equals(true)) {
            sendToClient(user.getUserId(), AlarmBadgeUnlockedDataDto.from(false), "new badge alarm exists");
        } else {
            sendToClient(user.getUserId(), AlarmBadgeUnlockedDataDto.from(true), "new badge alarm doesn't exists");
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
