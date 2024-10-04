package com.mmc.bookduck.global.jwt;

import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenException extends RuntimeException {
    private final ErrorCode errorCode;
}
