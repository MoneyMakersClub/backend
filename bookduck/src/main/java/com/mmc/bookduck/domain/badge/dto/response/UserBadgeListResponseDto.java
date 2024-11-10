package com.mmc.bookduck.domain.badge.dto.response;

import com.mmc.bookduck.domain.badge.dto.common.UserBadgeUnitDto;

import java.util.List;

public record UserBadgeListResponseDto(
        long currentReadCount,
        long currentArchiveCount,
        long currentOneLineCount,
        long currentLevel,
        List<UserBadgeUnitDto> readBadgeList,
        List<UserBadgeUnitDto> archiveBadgeList,
        List<UserBadgeUnitDto> oneLineBadgeList,
        List<UserBadgeUnitDto> levelBadgeList
) {
}
