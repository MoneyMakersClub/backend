package com.mmc.bookduck.domain.book.dto.common;

import jakarta.validation.constraints.NotNull;

public record BookUnitParseDto(@NotNull String title,
                               String author,
                               String imgPath,
                               String providerId) {
}