package com.mmc.bookduck.domain.homecard.dto.common;

import com.mmc.bookduck.domain.homecard.entity.CardType;

public record OneLineCardDto(
        Long cardId,
        Long cardIndex,
        CardType cardType,
        String title,
        String author,
        Double rating, // null일 수 있음
        String content
) implements HomeCardDto {
}
