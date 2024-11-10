package com.mmc.bookduck.domain.badge.dto.common;

import com.mmc.bookduck.domain.badge.entity.Badge;
import com.mmc.bookduck.domain.badge.entity.BadgeType;
import com.mmc.bookduck.domain.badge.entity.UserBadge;

import java.time.LocalDate;

public record UserBadgeUnitDto(
        BadgeType badgeType,
        String badgeName,
        String description,
        int unlockValue,
        LocalDate createdDate,
        Boolean isOwned
) {
    public static UserBadgeUnitDto from(Badge badge, UserBadge userBadge) {
        LocalDate localDate = userBadge != null ? userBadge.getCreatedTime().toLocalDate() : null;
        Boolean isOwned = userBadge != null;
        String unlockValueString = badge.getUnlockCondition().split("#")[1];
        Integer unlockValue = Integer.valueOf(unlockValueString);

        return new UserBadgeUnitDto(
                badge.getBadgeType(),
                badge.getBadgeName(),
                badge.getDescription(),
                unlockValue,
                localDate,
                isOwned
        );
    }
}
