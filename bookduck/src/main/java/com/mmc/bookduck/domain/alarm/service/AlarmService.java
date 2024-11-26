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
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;
    private final UserService userService;
    private final FCMService fcmService;

    // 최근 일반 Alarm 목록 가져오기
    @Transactional(readOnly = true)
    public PaginatedResponseDto<AlarmUnitDto> getCommonAlarms(Pageable pageable){
        User user = userService.getCurrentUser();

        Page<Alarm> alarmPage = alarmRepository.findByReceiverOrderByCreatedTimeDesc(user, pageable);
        Page<AlarmUnitDto> alarmUnitDtos =  alarmPage.map(AlarmUnitDto::new);
        return PaginatedResponseDto.from(alarmUnitDtos);
    }

    // Alarm 읽음 처리
    public void checkCommonAlarm(AlarmReadRequestDto requestDto) {
        User user = userService.getCurrentUser();
        Alarm alarm = alarmRepository.findById(requestDto.alarmId())
                .orElseThrow(()-> new CustomException(ErrorCode.ALARM_NOT_FOUND));
        if (!user.equals(alarm.getReceiver())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        alarm.readAlarm();
        alarmRepository.save(alarm);
        emitterService.sendToClientIfNewAlarmExists(user);
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

    // Alarm 전체 읽음처리
    public void checkAllCommonAlarm() {
        User user = userService.getCurrentUser();
        List<Alarm> alarms = alarmRepository.findAllByReceiverAndIsReadFalse(user);

        if (!alarms.isEmpty()) {
            alarms.forEach(Alarm::readAlarm);
            alarmRepository.saveAll(alarms);
        }
        emitterService.sendToClientIfNewAlarmExists(user);
    }
}
