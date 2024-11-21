package com.mmc.bookduck.global.initializer;

import com.mmc.bookduck.domain.badge.entity.BadgeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeData {
    READ_01(BadgeType.READ, "첫 책을 연 독서 새내기", "1"),
    READ_02(BadgeType.READ, "호기심 많은 독서 입문자", "5"),
    READ_03(BadgeType.READ, "책장을 넘기는 초보 독자", "10"),
    READ_04(BadgeType.READ, "활자에 빠진 탐독자", "20"),
    READ_05(BadgeType.READ, "책에 푹 빠진 애서가", "50"),
    READ_06(BadgeType.READ, "독서의 즐거움을 아는 애독가", "100"),
    READ_07(BadgeType.READ, "지식을 찾아가는 책 수집가", "200"),
    READ_08(BadgeType.READ, "지혜의 깊이를 아는 서재 주인", "300"),
    ARCHIVE_01(BadgeType.ARCHIVE, "첫 감상을 남긴 기록자", "1"),
    ARCHIVE_02(BadgeType.ARCHIVE, "느낌을 새기는 독서 팬", "5"),
    ARCHIVE_03(BadgeType.ARCHIVE, "생각을 담아내는 감상가", "10"),
    ARCHIVE_04(BadgeType.ARCHIVE, "추억을 남기는 독서 일기꾼", "20"),
    ARCHIVE_05(BadgeType.ARCHIVE, "글귀를 모으는 문장 수집가", "50"),
    ARCHIVE_06(BadgeType.ARCHIVE, "기록을 남기는 열성 독자", "100"),
    ARCHIVE_07(BadgeType.ARCHIVE, "깊이 있는 기록 전문가", "200"),
    ARCHIVE_08(BadgeType.ARCHIVE, "지식을 보관하는 서재 관리자", "300"),
    ONELINE_01(BadgeType.ONELINE, "첫 감상을 공유하는 독서가", "1"),
    ONELINE_02(BadgeType.ONELINE, "생각을 전하는 평론 초보자", "5"),
    ONELINE_03(BadgeType.ONELINE, "감성을 담아내는 감상 전문가", "10"),
    ONELINE_04(BadgeType.ONELINE, "한 줄 소감을 남기는 평가자", "20"),
    ONELINE_05(BadgeType.ONELINE, "책을 깊이 읽는 평론가", "50"),
    ONELINE_06(BadgeType.ONELINE, "생각을 나누는 독서 애호가", "100"),
    ONELINE_07(BadgeType.ONELINE, "깊이 있는 독서 평론 대가", "200"),
    ONELINE_08(BadgeType.ONELINE, "한줄 평론의 거장", "300"),
    LEVEL_01(BadgeType.LEVEL, "호기심 가득한 독서가", "5"),
    LEVEL_02(BadgeType.LEVEL, "열정적인 책 탐험가", "10"),
    LEVEL_03(BadgeType.LEVEL, "독서에 빠진 문장 수집가", "20"),
    LEVEL_04(BadgeType.LEVEL, "활자에 깊이 빠진 탐구자", "30"),
    LEVEL_05(BadgeType.LEVEL, "지혜를 찾아가는 서재 지킴이", "40"),
    LEVEL_06(BadgeType.LEVEL, "지식의 전당을 지키는 독서 거장", "50");

    private final BadgeType badgeType;
    private final String description;
    private final String unlockCondition;
}
