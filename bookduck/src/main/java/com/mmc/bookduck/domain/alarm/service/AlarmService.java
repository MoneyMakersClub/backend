package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.response.AlarmListResponseDto;
import com.mmc.bookduck.domain.alarm.dto.common.AlarmUnitDto;
import com.mmc.bookduck.domain.alarm.dto.ssedata.AlarmDefaultDataDto;
import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;
import com.mmc.bookduck.domain.alarm.entity.PushAlarmFormat;
import com.mmc.bookduck.domain.alarm.repository.AlarmRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.fcm.FCMService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final EmitterService emitterService;
    private final UserService userService;
    private final FCMService fcmService;

    // 최근 알림 목록 읽어오기
    public AlarmListResponseDto getAndReadRecentAlarms(){
        Pageable pageable = PageRequest.of(0, 30);
        List<AlarmUnitDto> alarmUnitDtos = getAndReadAlarms(pageable);
        return AlarmListResponseDto.fromAlarmUnitDto(alarmUnitDtos);
    }

    // Alarm 생성 읽어오기
    public void createAlarm(Alarm alarm, User receiver) {
        alarmRepository.save(alarm);
        emitterService.notify(receiver.getUserId(), AlarmDefaultDataDto.from(true), "new alarm");

        // 푸시알림 전송
        if (alarm.getAlarmType().isSendPush()) {
            String fcmToken = receiver.getFcmToken();
            if (fcmToken != null) {
                fcmService.sendPushMessage(fcmToken, PushAlarmFormat.valueOf(alarm.getAlarmType().name()));
            }
        }
    }

    public void deleteAllAlarmsOfMember(User user){
        alarmRepository.deleteAllBySender(user);
        alarmRepository.deleteAllByReceiver(user);
    }

    private List<AlarmUnitDto> getAndReadAlarms(Pageable pageable) {
        List<AlarmUnitDto> alarmUnitDtoList = new ArrayList<>();
        User currentUser = userService.getCurrentUser();
        // Announcement 유형 제외 가져오기
        Page<Alarm> alarmPage = alarmRepository.findByReceiverAndNotAnnouncementOrderByCreatedTimeDesc(currentUser, pageable);
        if (alarmPage != null && alarmPage.hasContent()) {
            for (Alarm alarm : alarmPage) {
                alarmUnitDtoList.add(new AlarmUnitDto(alarm));
                alarm.readAlarm();
            }
            alarmRepository.saveAll(alarmPage);
        }
        return alarmUnitDtoList;
    }

    public List<AlarmUnitDto> getRecentAnnouncements() {
        Pageable pageable = PageRequest.of(0, 30);
        // Announcement 유형만 가져오기
        Page<Alarm> announcementPage = alarmRepository.findByAlarmTypeOrderByCreatedTimeDesc(AlarmType.ANNOUNCEMENT, pageable);
        return announcementPage.stream().map(AlarmUnitDto::new).collect(Collectors.toList());
    }
}
