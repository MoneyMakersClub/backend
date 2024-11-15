package com.mmc.bookduck.domain.alarm.dto.response;

import com.mmc.bookduck.domain.alarm.dto.common.AlarmUnitDto;

import java.util.List;

public record AlarmListResponseDto(
        List<AlarmUnitDto> alarmList
) {
    public static AlarmListResponseDto fromAlarmUnitDto(List<AlarmUnitDto> alarmUnitDtos) {
        return new AlarmListResponseDto(alarmUnitDtos);
    }
}
