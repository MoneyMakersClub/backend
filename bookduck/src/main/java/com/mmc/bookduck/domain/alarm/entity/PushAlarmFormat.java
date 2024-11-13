package com.mmc.bookduck.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PushAlarmFormat {
    FRIEND_REQUEST("{0}님으로부터 친구요청이 도착했어요!", "북덕에서 확인하세요."),
    FRIEND_APPROVED("{0}님이 친구요청을 수락했어요.", "북덕에서 확인하세요."),

    ;

    private final String title;
    private final String body;
}
