package com.mmc.bookduck.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorDto> handleCustomException(CustomException e, HttpServletRequest request) {
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                e.getErrorCode().getStatus(),
                e.getErrorCode().name(),
                e.getErrorCode().getMessage(),
                request.getRequestURI()
        );
        log.info("메시지: [" + errorDto.errorCode() + "] " + errorDto.message());
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(e.getErrorCode().getStatus()));
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorDto> handleOAuth2AuthenticationException(OAuth2AuthenticationException e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.OAUTH2_LOGIN_FAILED;

        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.INVALID_ENUM_VALUE;
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorDto> handleBindException(MethodArgumentNotValidException e, HttpServletRequest request){
        String errorMessage = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode;
        if(errorMessage.contains("-")){
            String errorName = errorMessage.split("-")[0];
            errorMessage = e.getFieldError().getField() + ":" + errorMessage.split("-")[1];
            errorCode = ErrorCode.valueOf(errorName);
        } else {
            errorMessage = e.getFieldError().getField() + ":" + errorMessage;
            errorCode = ErrorCode.INVALID_INPUT_VALUE;
        }
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                errorCode.getStatus(),
                errorCode.name(),
                errorMessage,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDto,HttpStatusCode.valueOf(errorCode.getStatus()));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    protected ResponseEntity<ErrorDto> handleMissingRequestParamException(MissingServletRequestParameterException e, HttpServletRequest request) {
        ErrorCode errorCode = ErrorCode.MISSING_PARAMETER;
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                errorCode.getStatus(),
                errorCode.name(),
                e.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(errorCode.getStatus()));
    }

}
