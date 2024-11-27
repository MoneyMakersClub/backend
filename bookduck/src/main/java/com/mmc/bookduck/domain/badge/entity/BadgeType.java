package com.mmc.bookduck.domain.badge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadgeType {
    READ("열정적인 독자", "완독 수 {0}권 돌파!", "책 {0}권 읽기"),
    ARCHIVE("꼼꼼한 기록자", "기록 수 {0}개 돌파!", "기록 {0}개 작성하기"),
    ONELINE("성실한 평가자", "한줄평 수 {0}개 돌파!", "한줄평 {0}개 작성하기"),
    LEVEL("레벨업 마스터", "레벨 {0} 달성!", "레벨 {0}"),
    ;

    private final String title;
    private final String description;
    private final String modalMessage;
}
