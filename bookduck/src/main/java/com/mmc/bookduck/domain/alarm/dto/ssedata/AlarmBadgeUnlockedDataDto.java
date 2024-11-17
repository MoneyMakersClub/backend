package com.mmc.bookduck.domain.alarm.dto.ssedata;

public record AlarmBadgeUnlockedDataDto(
        Boolean isBadgeUnlockedChecked
) {
    public static AlarmBadgeUnlockedDataDto from (Boolean isNewAlarm) {
        return new AlarmBadgeUnlockedDataDto(isNewAlarm);
    }
}