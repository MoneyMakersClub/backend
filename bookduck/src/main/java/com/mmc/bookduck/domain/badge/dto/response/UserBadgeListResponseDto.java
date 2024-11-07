package com.mmc.bookduck.domain.badge.dto.response;

import com.mmc.bookduck.domain.badge.dto.common.UserBadgeUnitDto;

import java.util.List;

public record UserBadgeListResponseDto(
        int readBadgeCount,
        int archiveBadgeCount,
        int ratingBadgeCount,
        int levelBadgeCount,
        List<UserBadgeUnitDto> readBadgeList,
        List<UserBadgeUnitDto> archiveBadgeList,
        List<UserBadgeUnitDto> ratingBadgeList,
        List<UserBadgeUnitDto> levelBadgeList
) {
    public static UserBadgeListResponseDto from(List<UserBadgeUnitDto> readBadgeList,
                                                List<UserBadgeUnitDto> archiveBadgeList,
                                                List<UserBadgeUnitDto> ratingBadgeList,
                                                List<UserBadgeUnitDto> levelBadgeList) {
        return new UserBadgeListResponseDto(
                readBadgeList.size(),
                archiveBadgeList.size(),
                ratingBadgeList.size(),
                levelBadgeList.size(),
                readBadgeList,
                archiveBadgeList,
                ratingBadgeList,
                levelBadgeList
        );
    }
}
