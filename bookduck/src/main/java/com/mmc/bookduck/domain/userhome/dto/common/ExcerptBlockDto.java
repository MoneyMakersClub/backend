package com.mmc.bookduck.domain.userhome.dto.common;

public record ExcerptBlockDto(
        Long homeBlockId,
        Long excerptId,
        String title,
        String author,
        Long pageNumber, // null일 수 있음
        String content
) implements HomeBlockDto {
}
