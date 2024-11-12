package com.mmc.bookduck.global.fcm;

import com.mmc.bookduck.domain.alarm.entity.AlarmType;

import java.time.LocalDateTime;

public record ScheduleNotificationDto(
        Long alarmId,
        String nickname,
        AlarmType alarmType,
        String message,
        String url,
        LocalDateTime createdTime
) {}
