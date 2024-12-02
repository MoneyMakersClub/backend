package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.ssedata.BadgeModalInfo;
import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;
import com.mmc.bookduck.domain.badge.entity.BadgeType;
import com.mmc.bookduck.domain.badge.entity.UserBadge;
import com.mmc.bookduck.domain.book.entity.BookInfo;
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

    // 친구 요청 알림 생성
    public void createFriendRequestAlarm(User sender, User receiver) {
        String message = MessageFormat.format(AlarmType.FRIEND_REQUEST.getMessagePattern(), sender.getNickname());

        Alarm alarm = Alarm.builder()
                .alarmType(AlarmType.FRIEND_REQUEST)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .resourceName("User")
                .resourceId(sender.getUserId())
                .build();
        alarmService.createAlarm(alarm, receiver);
    }

    // 친구 수락 알림 생성
    public void createFriendApprovedAlarm(User sender, User receiver) {
        String message = MessageFormat.format(AlarmType.FRIEND_APPROVED.getMessagePattern(), sender.getNickname());

        Alarm alarm = Alarm.builder()
                .alarmType(AlarmType.FRIEND_APPROVED)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .resourceName("User")
                .resourceId(sender.getUserId())
                .build();
        alarmService.createAlarm(alarm, receiver);
    }

    // 한줄평 좋아요 알림 생성
    public void createOneLineLikeAlarm(User sender, User receiver, BookInfo bookInfo) {
        AlarmType alarmType = AlarmType.ONELINELIKE_ADDED;
        String message = MessageFormat.format(alarmType.getMessagePattern(), bookInfo.getTitle());

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .resourceName("BookInfo")
                .resourceId(bookInfo.getBookInfoId())
                .resourceValue(bookInfo.getTitle())
                .build();
        alarmService.createAlarm(alarm, receiver);
    }

    // 푸시 알림이 발생하지 않는 알림들
    // 레벨업 알림 생성
    public void createLevelUpAlarm(User receiver, int level) {
        AlarmType alarmType = AlarmType.LEVEL_UP;
        String message = MessageFormat.format(alarmType.getMessagePattern(), level);

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .receiver(receiver)
                .message(message)
                .resourceName("UserGrowth")
                .resourceValue(String.valueOf(level))
                .build();
        alarm.readAlarm();
        alarmService.createLevelUpAlarm(alarm, receiver, level);
    }

    // 뱃지 잠금해제 알림 생성
    public void createBadgeUnlockedAlarm(User receiver, UserBadge userBadge) {
        AlarmType alarmType = AlarmType.BADGE_UNLOCKED;
        BadgeType badgeType =  userBadge.getBadge().getBadgeType();
        String message = MessageFormat.format(alarmType.getMessagePattern(), badgeType.getTitle());

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .receiver(receiver)
                .message(message)
                .resourceName("Badge")
                .resourceValue(badgeType.getTitle())
                .build();
        alarm.readAlarm();

        String description = String.format("%s를 달성하여\n%s 배지를 얻었어요.",
                MessageFormat.format(badgeType.getModalMessage(), userBadge.getBadge().getUnlockCondition()),
                badgeType.getTitle());
        BadgeModalInfo badgeModalInfo = new BadgeModalInfo(badgeType, userBadge.getBadge().getBadgeName(), description);
        alarmService.createBadgeUnlockedAlarm(alarm, receiver, badgeModalInfo);
    }

    // 레벨업 알림 생성
    public void createItemUnlockedAlarm(User receiver) {
        AlarmType alarmType = AlarmType.ITEM_UNLOCKED;
        String message = alarmType.getMessagePattern();

        Alarm alarm = Alarm.builder()
                .alarmType(alarmType)
                .receiver(receiver)
                .message(message)
                .resourceName("Item")
                .build();
        alarm.readAlarm();
        alarmService.createAlarm(alarm, receiver);
    }
}
