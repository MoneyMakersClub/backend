package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.request.AlarmReadRequestDto;
import com.mmc.bookduck.domain.alarm.dto.common.AlarmUnitDto;
import com.mmc.bookduck.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.repository.AlarmRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.common.PaginatedResponseDto;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.fcm.FCMService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;
    private final UserService userService;
    private final FCMService fcmService;

    // 최근 일반 Alarm 목록 읽기
    public PaginatedResponseDto<AlarmUnitDto> getCommonAlarms(Pageable pageable){
        User user = userService.getCurrentUser();

        Page<Alarm> alarmPage = alarmRepository.findByReceiverOrderByCreatedTimeDesc(user, pageable);
        Page<AlarmUnitDto> alarmUnitDtos =  alarmPage.map(AlarmUnitDto::new);
        return PaginatedResponseDto.from(alarmUnitDtos);
    }

    // Alarm 읽음처리
    public void checkCommonAlarm(AlarmReadRequestDto requestDto) {
        User user = userService.getCurrentUser();
        Alarm alarm = alarmRepository.findById(requestDto.alarmId())
                .orElseThrow(()-> new CustomException(ErrorCode.ALARM_NOT_FOUND));
        if (user.equals(alarm.getReceiver())) {
            alarm.readAlarm();
            alarmRepository.save(alarm);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
    }

    // Alarm 생성
    public void createAlarm(Alarm alarm, User receiver) {
        alarmRepository.save(alarm);
        emitterService.notify(
                receiver.getUserId(),
                AlarmDefaultDataDto.from(false, receiver.getIsAnnouncementChecked(), receiver.getIsItemUnlockedChecked()),
                "new alarm"
        );

        // 푸시알림 전송
        if (alarm.getAlarmType().isSendPush()) {
            String fcmToken = receiver.getFcmToken();
            if (fcmToken != null) {
                fcmService.sendPushMessage(fcmToken, alarm.getMessage());
            }
        }
    }

    public void deleteAllAlarmsOfMember(User user){
        alarmRepository.deleteAllBySender(user);
        alarmRepository.deleteAllByReceiver(user);
    }
}
