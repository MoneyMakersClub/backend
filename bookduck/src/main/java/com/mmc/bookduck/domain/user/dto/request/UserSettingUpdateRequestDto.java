package com.mmc.bookduck.domain.user.dto.request;

import com.mmc.bookduck.domain.user.entity.RecordFont;

public record UserSettingUpdateRequestDto(
        Boolean isPushAlarmEnabled,
        Boolean isFriendRequestEnabled,
        RecordFont recordFont
) {
}
