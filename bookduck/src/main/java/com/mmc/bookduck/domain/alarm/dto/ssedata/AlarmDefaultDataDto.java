package com.mmc.bookduck.domain.alarm.dto.ssedata;

public record AlarmDefaultDataDto(
        Boolean isCommonAlarmChecked,
        Boolean isAnnouncementChecked,
        Boolean isItemUnlockedChecked
) {
    public static AlarmDefaultDataDto from (boolean isCommonAlarmChecked,
                                            boolean isAnnouncementChecked,
                                            boolean isItemUnlockedChecked) {
        return new AlarmDefaultDataDto(isCommonAlarmChecked, isAnnouncementChecked, isItemUnlockedChecked);
    }
}