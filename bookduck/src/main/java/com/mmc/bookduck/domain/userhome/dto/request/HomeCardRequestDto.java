package com.mmc.bookduck.domain.userhome.dto.request;

import com.mmc.bookduck.domain.userhome.entity.CardType;
import jakarta.validation.constraints.NotNull;

public record HomeCardRequestDto(
        @NotNull CardType cardType,
        @NotNull Long resourceId1,
        Long resourceId2,
        String text1,
        String text2,
        String text3
) {
}