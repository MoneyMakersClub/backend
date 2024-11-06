package com.mmc.bookduck.domain.user.dto.response;

import java.util.List;

public record UserGrowthInfoResponseDto (
        int level,
        List<String> missions
) {
}