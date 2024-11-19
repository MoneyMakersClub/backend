package com.mmc.bookduck.domain.book.dto.common;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BookUnitParseDto(@NotNull String title,
                               String author,
                               String imgPath,
                               String providerId) {
}
