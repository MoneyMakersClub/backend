package com.mmc.bookduck.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public enum AlarmType {
    GENERAL("", false),
    ANNOUNCEMENT("[공지]", true),
    FRIEND_REQUEST("{0}님으로부터 친구요청이 도착했어요!", true),
    FRIEND_APPROVED("{0}님이 친구요청을 수락했어요.", true),
    ONELINELIKE_ADDED("{0}의 한줄평에 좋아요가 눌렸어요.", true),
    LEVEL_UP("야호! 오리가 Lv.{0}로 성장했어요.", false),
    BADGE_UNLOCKED("축하합니다! \uD83C\uDF89 새로운 {0} 뱃지를 획득했어요.", false),
    ITEM_UNLOCKED("새로운 아이템을 획득했어요. 얼른 착용해봐요!", false),
    PUSH("",true), // 기타 푸시알림
    ;

    private final String messagePattern;
    private final boolean sendPush;

    // boldText 생성 메서드
    public String extractBoldText(Alarm alarm) {
        return switch (this) {
            case FRIEND_REQUEST, FRIEND_APPROVED, ONELINELIKE_ADDED, BADGE_UNLOCKED ->
                    MessageFormat.format("{0}", alarm.getSender().getNickname());
            case LEVEL_UP ->
                    MessageFormat.format("{0}", alarm.getResourceValue());
            default -> "";
        };
    }
}
