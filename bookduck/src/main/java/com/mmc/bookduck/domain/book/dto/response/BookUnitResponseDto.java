package com.mmc.bookduck.domain.book.dto.response;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BookUnitResponseDto(
        @NotNull String title,
        List<String> author,
        String imgPath,
        String providerId
) {
}