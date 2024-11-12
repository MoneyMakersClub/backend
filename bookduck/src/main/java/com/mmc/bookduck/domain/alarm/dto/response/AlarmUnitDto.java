package com.mmc.bookduck.domain.alarm.dto.response;


import com.mmc.bookduck.domain.alarm.entity.Alarm;

import java.time.LocalDateTime;

public record AlarmUnitDto(
        Boolean isRead,
        String message,
        LocalDateTime createdTime,
        String nickname
) {
    public static AlarmUnitDto from(Alarm alarm, String message) {
        return new AlarmUnitDto(
                alarm.isRead(),
                message,
                alarm.getCreatedTime(),
                alarm.getSender().getNickname()
        );
    }
}