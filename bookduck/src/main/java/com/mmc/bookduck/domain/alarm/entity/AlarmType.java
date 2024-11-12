package com.mmc.bookduck.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    GENERAL(""),
    ANNOUNCEMENT("[공지]"),
    FRIEND_REQUEST("{0}님으로부터 친구요청이 도착했어요!"),
    FRIEND_APPROVED("{0}님이 친구요청을 수락했어요."),
    ONELINEHEART_ADDED("{0}님이 한줄평에 좋아요를 눌렀어요."),
    LEVEL_UP("야호! 오리가 Lv.{0}로 성장했어요."),
    ITEM_UNLOCKED("축하합니다! 새 아이템을 획득했어요."),
    BADGE_UNLOCKED("축하합니다! \uD83C\uDF89 {0} 뱃지를 획득했어요."),
    PUSH("[푸시]"),
    ;

    private final String messagePattern;
}
