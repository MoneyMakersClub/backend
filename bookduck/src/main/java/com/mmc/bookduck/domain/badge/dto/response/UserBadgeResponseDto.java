package com.mmc.bookduck.domain.badge.dto.response;

import com.mmc.bookduck.domain.badge.dto.common.UserBadgeUnitDto;

import java.util.List;

public record UserBadgeResponseDto (
        List<UserBadgeUnitDto> readBadgeList,
        List<UserBadgeUnitDto> archiveBadgeList,
        List<UserBadgeUnitDto> orlBadgeList,
        List<UserBadgeUnitDto> levelBadgeList
) {
}
