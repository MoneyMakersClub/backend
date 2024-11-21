package com.mmc.bookduck.domain.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemUnlockCondition {
    // 장르 조합 그룹화
    FICTION_LITERARY_HUMANITIES_SCIENCE("FICTION+LITERARY+HUMANITIES+SCIENCE", 5),
    SCIENCE("SCIENCE", 5),
    ART_COMICS("ART+COMICS", 5),
    TRAVEL("TRAVEL", 5),
    ARCHITECTURE_TECHNOLOGY("ARCHITECTURE+TECHNOLOGY", 5),
    COMPUTER("COMPUTER", 5),
    HEALTH("HEALTH", 5),
    OTHERS("OTHERS", 0),
    HISTORY("HISTORY", 5),
    BUSINESS_SOCIETY("BUSINESS+SOCIETY", 5),
    HOME_COOKING("HOME_COOKING", 5);

    private final String genres;
    private final int requiredCount;
}
