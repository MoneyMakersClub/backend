package com.mmc.bookduck.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;

public enum LoginType {
    KAKAO,
    GOOGLE
    ;

    @JsonCreator
    public static LoginType from(String s) {
        try {
            return LoginType.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ENUM_VALUE);
        }
    }
}
