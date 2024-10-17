package com.mmc.bookduck.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.exception.ErrorDto;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode, String requestUri, boolean redirectToRefresh) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // ErrorDto 생성
        ErrorDto errorDto = new ErrorDto(
                LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                errorCode.getStatus(),
                errorCode.name(),
                errorCode.getMessage(),
                requestUri
        );

        // 만약 리프레시 토큰 경로로 리다이렉트하고 싶다면 Location 헤더에 "/refresh" 추가
        if (redirectToRefresh) {
            response.setHeader("Location", "/refresh");
        }

        try {
            String jsonResponse = objectMapper.writeValueAsString(errorDto);
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
