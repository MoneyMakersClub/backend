package com.mmc.bookduck.domain.homecard.dto.common;

import com.mmc.bookduck.domain.homecard.entity.CardType;

public record OneLineCardDto(
        Long cardId,
        Long cardIndex,
        CardType cardType,
        String oneLineContent,
        double rating,
        String title,
        String author
) implements HomeCardDto {
}
