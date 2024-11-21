package com.mmc.bookduck.domain.badge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadgeType {
    READ("열정적인 독자", "완독 수 {0}권 돌파!"),
    ARCHIVE("꼼꼼한 기록자", "기록카드 수 {0}개 돌파!"),
    ONELINE("성실한 평가자", "한줄평 수 {0}개 돌파!"),
    LEVEL("레벨업 마스터", "레벨 {0} 달성!"),
    ;

    private final String title;
    private final String description;
}
