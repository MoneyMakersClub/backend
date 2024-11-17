package com.mmc.bookduck.domain.homecard.dto.common;

import com.mmc.bookduck.domain.homecard.entity.CardType;

public record BookWithMemoCardDto(
        Long cardId,
        Long cardIndex,
        CardType cardType,
        String imgPath1,
        String imgPath2,
        String text1
) implements HomeCardDto {
}