package com.mmc.bookduck.domain.userhome.dto.common;

import com.mmc.bookduck.domain.userhome.entity.CardType;

public record ExcerptCardDto(
        Long cardId,
        Long cardIndex,
        CardType cardType,
        String title,
        String author,
        Long pageNumber, // null일 수 있음
        String excerptContent
) implements HomeCardDto {
}
