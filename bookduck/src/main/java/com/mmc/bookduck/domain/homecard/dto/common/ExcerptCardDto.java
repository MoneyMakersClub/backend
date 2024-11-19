package com.mmc.bookduck.domain.homecard.dto.common;

import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.homecard.entity.CardType;

public record ExcerptCardDto(
        Long cardId,
        Long cardIndex,
        CardType cardType,
        String excerptContent,
        Long pageNumber, // null일 수 있음
        Visibility visibility,
        String title,
        String author
) implements HomeCardDto {
}
