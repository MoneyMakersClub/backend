package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;
import com.mmc.bookduck.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmByTypeService {
    private final AlarmService alarmService;

    // 친구 요청/친구 수락 알림 생성
    public void createFriendAlarm(User sender, User receiver, AlarmType alarmType) {
        String message = MessageFormat.format(alarmType.getMessagePattern(), sender.getNickname());

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .resourceName("User")
                .resourceId(sender.getUserId())
                .build();
        alarmService.createAlarm(alarm, receiver);
    }

    // 한줄평 좋아요 알림 생성
    public void createOneLineHeartAlarm(User sender, User receiver, Long bookInfoId) {
        AlarmType alarmType = AlarmType.ONELINEHEART_ADDED;
        String message = MessageFormat.format(alarmType.getMessagePattern(), sender.getNickname());

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .resourceName("BookInfo")
                .resourceId(bookInfoId)
                .build();
        alarmService.createAlarm(alarm, receiver);
    }

    // 레벨업 알림 생성
    public void createLevelUpAlarm(User receiver, Long level) {
        AlarmType alarmType = AlarmType.LEVEL_UP;
        String message = MessageFormat.format(alarmType.getMessagePattern(), level);

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .receiver(receiver)
                .message(message)
                .resourceName("UserGrowth")
                .resourceValue(level.toString())
                .build();
        alarmService.createAlarm(alarm, receiver);
    }

    // 아이템 잠금해제 알림 생성
    public void createItemUnlockAlarm(User receiver) {
        AlarmType alarmType = AlarmType.ITEM_UNLOCKED;

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .receiver(receiver)
                .message(alarmType.getMessagePattern())
                .resourceName("Item")
                .build();
        alarmService.createAlarm(alarm, receiver);
    }

    // 뱃지 잠금해제 알림 생성
    public void createBadgeUnlockAlarm(User receiver) {
        AlarmType alarmType = AlarmType.BADGE_UNLOCKED;

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .receiver(receiver)
                .message(alarmType.getMessagePattern())
                .resourceName("Badge")
                .build();
        alarmService.createAlarm(alarm, receiver);
    }
}
