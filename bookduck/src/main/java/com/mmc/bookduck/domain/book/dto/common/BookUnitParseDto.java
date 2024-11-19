package com.mmc.bookduck.domain.book.dto.common;

import jakarta.validation.constraints.NotNull;

public record BookUnitParseDto(
        @NotNull String title,
        List<String> author,
        String imgPath,
        String providerId
) {
}
