package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.request.AlarmReadRequestDto;
import com.mmc.bookduck.domain.alarm.dto.common.AlarmUnitDto;
import com.mmc.bookduck.domain.alarm.dto.ssedata.BadgeModalInfo;
import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.repository.AlarmRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserSetting;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.domain.user.service.UserSettingService;
import com.mmc.bookduck.global.common.PaginatedResponseDto;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.fcm.FCMService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;
    private final UserService userService;
    private final FCMService fcmService;
    private final UserSettingService userSettingService;

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
        // 알림 수신자 확인
        if (!user.equals(alarm.getReceiver())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_REQUEST);
        }
        // 알림 새롭게 읽은 경우만 업데이트 및 SSE 알림 전송
        if (!alarm.isRead()) {
            alarm.readAlarm();
            alarmRepository.save(alarm);
            emitterService.sendToClientDefaultAlarm(user);
        }
    }

    // Alarm 생성
    public void createAlarm(Alarm alarm, User receiver) {
        // 알림 저장
        alarmRepository.save(alarm);
        // SSE 알림을 클라이언트로 전송
        emitterService.sendToClientDefaultAlarm(receiver);
        // 푸시 알림 전송
        sendPushNotificationIfEnabled(receiver, alarm);
    }

    // Alarm 생성
    public void createLevelUpAlarm(Alarm alarm, User receiver, int level) {
        // 알림 저장
        alarmRepository.save(alarm);
        // SSE 알림을 클라이언트로 전송
        emitterService.sendToClientLevelUpAlarm(receiver, level);
    }

    // Alarm 생성
    public void createBadgeUnlockedAlarm(Alarm alarm, User receiver, BadgeModalInfo badgeModalInfo) {
        // 알림 저장
        alarmRepository.save(alarm);
        // SSE 알림을 클라이언트로 전송
        emitterService.sendToClientBadgeUnlockedAlarm(receiver, badgeModalInfo);
    }

    // 푸시 알림 전송
    private void sendPushNotificationIfEnabled(User receiver, Alarm alarm) {
        UserSetting userSetting = userSettingService.getUserSettingByUser(receiver);
        if (userSetting.isPushAlarmEnabled() && alarm.getAlarmType().isSendPush()) {
            String fcmToken = receiver.getFcmToken();
            if (fcmToken != null) {
                fcmService.sendPushMessage(fcmToken, alarm.getMessage());
            }
        }
    }

    public void deleteAlarmsOfUser(User user){
        alarmRepository.deleteBySender(user);
        alarmRepository.deleteByReceiver(user);
    }

    // Alarm 전체 읽음처리
    public void checkAllCommonAlarm() {
        User user = userService.getCurrentUser();
        List<Alarm> alarms = alarmRepository.findAllByReceiverAndIsReadFalse(user);

        if (!alarms.isEmpty()) {
            alarms.forEach(Alarm::readAlarm);
            alarmRepository.saveAll(alarms);
        }
        // SSE 알림 전송
        emitterService.sendToClientDefaultAlarm(user);
    }
}
