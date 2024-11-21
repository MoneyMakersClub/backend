package com.mmc.bookduck.domain.badge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadgeType {
    READ("완독 수 {0}권 돌파!"),
    ARCHIVE("기록카드 수 {0}개 돌파!"),
    ONELINE("한줄평 수 {0}개 돌파!"),
    LEVEL("레벨 {0} 달성!"),
    ;

    private final String description;
}
