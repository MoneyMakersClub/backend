package com.mmc.bookduck.global.exception;

import lombok.Getter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

@Getter
public class CustomOAuth2AuthenticationException extends OAuth2AuthenticationException {
    private final ErrorCode errorCode;

    public CustomOAuth2AuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
