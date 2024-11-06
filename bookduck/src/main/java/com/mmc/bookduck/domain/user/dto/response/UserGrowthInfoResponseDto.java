package com.mmc.bookduck.domain.user.dto.response;

public record UserGrowthInfoResponseDto (
        long level,
        long expInCurrentLevel,
        long expToNextLevel
) {
}