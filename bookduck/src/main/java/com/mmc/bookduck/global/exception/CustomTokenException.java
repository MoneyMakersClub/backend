package com.mmc.bookduck.global.exception;

public class CustomTokenException extends CustomException {
    public CustomTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
