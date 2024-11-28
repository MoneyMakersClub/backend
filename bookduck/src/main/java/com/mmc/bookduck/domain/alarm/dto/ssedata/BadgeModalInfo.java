package com.mmc.bookduck.domain.alarm.dto.ssedata;

import com.mmc.bookduck.domain.badge.entity.BadgeType;

public record BadgeModalInfo(
        BadgeType badgeType,
        String badgeName,
        String description
) {
}
