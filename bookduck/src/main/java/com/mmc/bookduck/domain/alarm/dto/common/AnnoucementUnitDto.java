package com.mmc.bookduck.domain.alarm.dto.common;

import com.mmc.bookduck.domain.alarm.entity.Alarm;

import java.time.LocalDateTime;

public record AnnoucementUnitDto (
        Long alarmId,
        Boolean isRead,
        LocalDateTime createdTime,
        String content
) {
    public AnnoucementUnitDto(Alarm alarm) {
        this(
                alarm.getAlarmId(),
                alarm.isRead(),
                alarm.getCreatedTime(),
                alarm.getMessage()
        );
    }
}