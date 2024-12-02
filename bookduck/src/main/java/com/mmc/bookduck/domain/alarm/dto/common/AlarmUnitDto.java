package com.mmc.bookduck.domain.alarm.dto.common;


import com.mmc.bookduck.domain.alarm.entity.Alarm;
import com.mmc.bookduck.domain.alarm.entity.AlarmType;

import java.time.LocalDateTime;

public record AlarmUnitDto(
        Long alarmId,
        Boolean isRead,
        AlarmType alarmType,
        LocalDateTime createdTime,
        String boldText,
        String resourceName,
        Long resourceId
) {
    public AlarmUnitDto(Alarm alarm, String boldText) {
        this(
                alarm.getAlarmId(),
                alarm.isRead(),
                alarm.getAlarmType(),
                alarm.getCreatedTime(),
                boldText,
                alarm.getResourceName(),
                alarm.getResourceId()
        );
    }
}