package com.mmc.bookduck.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.exception.ErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object exception = request.getAttribute("exception");
        if(exception instanceof ErrorCode){
            ErrorCode errorCode = (ErrorCode) exception;
            setResponse(request, response, errorCode);
            return;
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }

    private void setResponse(HttpServletRequest request, HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().toString(),
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMessage(),
                request.getRequestURI()
        );
        // ObjectMapper 설정 및 JSON 문자열 생성
        String jsonString = objectMapper.writeValueAsString(errorDto);

        // JSON 문자열 출력
        response.getWriter().println(jsonString);
    }
}