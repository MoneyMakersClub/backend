package com.mmc.bookduck.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    FRIEND_REQUEST("{0}님이 친구 요청을 보냈어요."),
    FRIEND_APPROVED("{0}님과 친구가 되었어요."),
    REVIEW_HEART_ADDED("{0}님이 내 기록에 좋아요를 눌렀어요."),
    ANNOUNCEMENT(""),
    EVENT(""),
    ;

    private final String messagePattern;
}
