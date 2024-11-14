package com.mmc.bookduck.domain.userhome.dto.request;

import com.mmc.bookduck.domain.userhome.entity.CardType;
import jakarta.validation.constraints.NotNull;

public record UserHomeCardRequestDto(
        @NotNull CardType cardType,
        @NotNull Long resourceId1,
        Long resourceId2,
        String text1,
        String text2
) {
}