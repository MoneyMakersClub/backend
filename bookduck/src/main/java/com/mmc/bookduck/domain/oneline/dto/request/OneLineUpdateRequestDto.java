package com.mmc.bookduck.domain.oneline.dto.request;

import jakarta.validation.constraints.NotNull;

public record OneLineUpdateRequestDto(
        @NotNull String oneLineContent
) {
}
