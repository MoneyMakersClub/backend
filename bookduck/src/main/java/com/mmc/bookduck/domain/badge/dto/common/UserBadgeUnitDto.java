package com.mmc.bookduck.domain.badge.dto.common;

import com.mmc.bookduck.domain.badge.entity.UserBadge;

import java.time.LocalDate;

public record UserBadgeUnitDto(
        Long badgeId,
        String description,
        LocalDate createdDate
) {
    public static UserBadgeUnitDto from(UserBadge userBadge) {
        return new UserBadgeUnitDto(
                userBadge.getBadge().getBadgeId(),
                userBadge.getBadge().getDescription(),
                userBadge.getCreatedTime().toLocalDate()
        );
    }
}
