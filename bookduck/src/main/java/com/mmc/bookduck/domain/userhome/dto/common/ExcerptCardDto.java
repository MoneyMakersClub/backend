package com.mmc.bookduck.domain.userhome.dto.common;

public record ExcerptCardDto(
        Long homeCardId,
        Long cardIndex,
        Long resourceId1,
        String title,
        String author,
        Long pageNumber, // null일 수 있음
        String content
) implements HomeCardDto {
}
