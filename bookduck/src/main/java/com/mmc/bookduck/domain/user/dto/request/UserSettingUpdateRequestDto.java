package com.mmc.bookduck.domain.user.dto.request;

import com.mmc.bookduck.domain.user.entity.RecordFont;
import jakarta.validation.constraints.NotNull;

public record UserSettingUpdateRequestDto(
        @NotNull Boolean isPushAlarmEnabled,
        @NotNull Boolean isFriendRequestEnabled,
        @NotNull RecordFont recordFont
) {
}
