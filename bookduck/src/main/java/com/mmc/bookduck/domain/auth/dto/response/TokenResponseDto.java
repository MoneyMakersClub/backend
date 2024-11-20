package com.mmc.bookduck.domain.auth.dto.response;

public record TokenResponseDto(
        String accessToken,
        long accessTokenMaxAge
) {
}
