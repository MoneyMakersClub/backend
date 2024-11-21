package com.mmc.bookduck.global.initializer;

import com.mmc.bookduck.domain.badge.entity.BadgeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeData {
    READ_01(BadgeType.READ, "1"),
    READ_02(BadgeType.READ, "5"),
    READ_03(BadgeType.READ, "10"),
    READ_04(BadgeType.READ, "20"),
    READ_05(BadgeType.READ, "50"),
    READ_06(BadgeType.READ, "100"),
    READ_07(BadgeType.READ, "200"),
    READ_08(BadgeType.READ, "300"),
    ARCHIVE_01(BadgeType.ARCHIVE, "1"),
    ARCHIVE_02(BadgeType.ARCHIVE, "5"),
    ARCHIVE_03(BadgeType.ARCHIVE, "10"),
    ARCHIVE_04(BadgeType.ARCHIVE, "20"),
    ARCHIVE_05(BadgeType.ARCHIVE, "50"),
    ARCHIVE_06(BadgeType.ARCHIVE, "100"),
    ARCHIVE_07(BadgeType.ARCHIVE, "200"),
    ARCHIVE_08(BadgeType.ARCHIVE, "300"),
    ONELINE_01(BadgeType.ONELINE, "1"),
    ONELINE_02(BadgeType.ONELINE, "5"),
    ONELINE_03(BadgeType.ONELINE, "10"),
    ONELINE_04(BadgeType.ONELINE, "20"),
    ONELINE_05(BadgeType.ONELINE, "50"),
    ONELINE_06(BadgeType.ONELINE, "100"),
    ONELINE_07(BadgeType.ONELINE, "200"),
    ONELINE_08(BadgeType.ONELINE, "300"),
    LEVEL_01(BadgeType.LEVEL, "5"),
    LEVEL_02(BadgeType.LEVEL, "10"),
    LEVEL_03(BadgeType.LEVEL, "20"),
    LEVEL_04(BadgeType.LEVEL, "30"),
    LEVEL_05(BadgeType.LEVEL, "40"),
    LEVEL_06(BadgeType.LEVEL, "50");

    private final BadgeType badgeType;
    private final String unlockCondition;
}