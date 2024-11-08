package com.mmc.bookduck.domain.badge.dto.response;

import com.mmc.bookduck.domain.badge.dto.common.UserBadgeUnitDto;

import java.util.List;

public record UserBadgeListResponseDto(
        int readBadgeTotalCount,
        int archiveBadgeTotalCount,
        int ratingBadgeTotalCount,
        int levelBadgeTotalCount,
        List<UserBadgeUnitDto> readBadgeOwnedList,
        List<UserBadgeUnitDto> archiveBadgeOwnedList,
        List<UserBadgeUnitDto> ratingBadgeOwnedList,
        List<UserBadgeUnitDto> levelBadgeOwnedList
) {
}
