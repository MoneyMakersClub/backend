package com.mmc.bookduck.domain.alarm.dto.common;


import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;

import java.time.LocalDateTime;

public record AlarmUnitDto(
        Boolean isRead,
        AlarmType alarmType,
        LocalDateTime createdTime,
        String boldText,
        String resourceName,
        Long resourceId
) {
    public AlarmUnitDto(Alarm alarm) {
        this(
                alarm.isRead(),
                alarm.getAlarmType(),
                alarm.getCreatedTime(),
                alarm.getSender() != null ? alarm.getSender().getNickname() : alarm.getResourceValue(),
                alarm.getResourceName(),
                alarm.getResourceId()
        );
    }
}