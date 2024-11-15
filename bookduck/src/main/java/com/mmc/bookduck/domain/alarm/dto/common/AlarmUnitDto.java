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
    public static AlarmUnitDto from(Alarm alarm) {
        String boldText = null;
        if (alarm.getSender() != null)
            boldText = alarm.getSender().getNickname();
        else if (alarm.getResourceValue() != null)
            boldText = alarm.getResourceValue();
        return new AlarmUnitDto(
                alarm.isRead(),
                alarm.getAlarmType(),
                alarm.getCreatedTime(),
                boldText,
                alarm.getResourceName(),
                alarm.getResourceId()
        );
    }
}