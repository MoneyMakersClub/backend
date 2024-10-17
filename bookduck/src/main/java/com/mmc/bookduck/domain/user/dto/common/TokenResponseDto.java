package com.mmc.bookduck.domain.user.dto.common;

public record TokenResponseDto(String accessToken, String refreshToken, long accessTokenMaxAge, long refreshTokenMaxAge) {
}
