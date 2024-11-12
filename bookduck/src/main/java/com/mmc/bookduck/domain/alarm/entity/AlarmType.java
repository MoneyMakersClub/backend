package com.mmc.bookduck.domain.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    GENERAL("", false),
    ANNOUNCEMENT("[공지]", false),
    FRIEND_REQUEST("{0}님으로부터 친구요청이 도착했어요!", true),
    FRIEND_APPROVED("{0}님이 친구요청을 수락했어요.", true),
    ONELINEHEART_ADDED("{0} 책의 한줄평에 좋아요를 눌렀어요.", false),
    LEVEL_UP("야호! 오리가 Lv.{0}로 성장했어요.", false),
    ITEM_UNLOCKED("축하합니다! 새 아이템을 획득했어요.", false),
    BADGE_UNLOCKED("축하합니다! \uD83C\uDF89 새 뱃지를 획득했어요.", false),
    PUSH("",true), // 기타 푸시알림
    ;

    private final String messagePattern;
    private final boolean sendPush;


    // 메시지 생성 메서드
//    public String generateMessage(Alarm alarm) {
//        return switch (this) {
//            case FRIEND_REQUEST, FRIEND_APPROVED, ONELINEHEART_ADDED, BADGE_UNLOCKED ->
//                    MessageFormat.format(messagePattern, alarm.getSender().getNickname());
//            case LEVEL_UP ->
//                    MessageFormat.format(messagePattern, alarm.getResourceValue());
//            default -> messagePattern;
//        };
//    }
}
