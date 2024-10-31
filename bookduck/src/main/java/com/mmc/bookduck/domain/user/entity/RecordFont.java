package com.mmc.bookduck.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;

public enum RecordFont {
    NANUMGOTHIC,
    NANUMMYEONGJO,
    RIDIBATANG
    ;

    // ex. JSON 데이터 "NANUMGOTHIC" 문자열 -> RecordFont.NANUMGOTHIC으로 변환
    @JsonCreator
    public static RecordFont from(String s) {
        try {
            return RecordFont.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
