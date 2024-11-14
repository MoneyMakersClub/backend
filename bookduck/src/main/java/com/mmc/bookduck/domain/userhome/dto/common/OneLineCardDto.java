package com.mmc.bookduck.domain.userhome.dto.common;

public record OneLineCardDto(
        Long homeCardId,
        Long cardIndex,
        Long resourceId1,
        String title,
        String author,
        Double rating, // null일 수 있음
        String content
) implements HomeCardDto {
}
