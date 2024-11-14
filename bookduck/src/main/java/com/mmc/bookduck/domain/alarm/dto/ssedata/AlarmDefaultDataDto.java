package com.mmc.bookduck.domain.alarm.dto.ssedata;

public record AlarmDefaultDataDto(
        Boolean isNewAlarm
) {
    public static AlarmDefaultDataDto from (Boolean isNewAlarm) {
        return new AlarmDefaultDataDto(isNewAlarm);
    }
}