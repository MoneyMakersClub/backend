package com.mmc.bookduck.domain.alarm.dto.ssedata;

public record AlarmDefaultDataDto(
        Boolean isCommonAlarmChecked,
        Boolean isAnnouncementChecked,
        Boolean isItemUnlockedChecked,
        Boolean isLevelUpChecked,
        Boolean isBadgeUnlockedChecked,
        Integer newLevel,
        BadgeModalInfo newBadgeInfo
) {
    public static AlarmDefaultDataDto fromDefault (boolean isCommonAlarmChecked, boolean isAnnouncementChecked, boolean isItemUnlockedChecked) {
        return new AlarmDefaultDataDto(isCommonAlarmChecked, isAnnouncementChecked, isItemUnlockedChecked, true, true, null, null);
    }

    public static AlarmDefaultDataDto fromLevelUp (boolean isCommonAlarmChecked, boolean isAnnouncementChecked, int newLevel) {
        return new AlarmDefaultDataDto(isCommonAlarmChecked, isAnnouncementChecked, true, false, true, newLevel, null);
    }

    public static AlarmDefaultDataDto fromBadgeUnlocked (boolean isCommonAlarmChecked, boolean isAnnouncementChecked, BadgeModalInfo newBadgeInfo) {
        return new AlarmDefaultDataDto(isCommonAlarmChecked, isAnnouncementChecked, true, true, false, null, newBadgeInfo);
    }
}