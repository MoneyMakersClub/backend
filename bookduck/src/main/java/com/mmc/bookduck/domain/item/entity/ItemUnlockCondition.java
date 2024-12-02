package com.mmc.bookduck.domain.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemUnlockCondition {
    FICTION_LITERARY_HUMANITIES("FICTION+LITERARY+HUMANITIES"),
    SCIENCE("SCIENCE"),
    ART_COMICS("ART+COMICS"),
    TRAVEL("TRAVEL"),
    ARCHITECTURE_TECHNOLOGY("ARCHITECTURE+TECHNOLOGY"),
    COMPUTER("COMPUTER"),
    HEALTH("HEALTH"),
    OTHERS("OTHERS"),
    HISTORY("HISTORY"),
    BUSINESS_SOCIETY("BUSINESS+SOCIETY"),
    HOME_COOKING("HOME_COOKING"),
    SELF_HELP("SELF_HELP"),
    ART_HOBBY("ART+HOBBY");

    private final String genres;
}