package com.mmc.bookduck.domain.userhome.dto.common;

public record OneLineBlockDto(
        Long homeBlockId,
        Long oneLineId,
        String title,
        String author,
        Double rating, // null일 수 있음
        String content
) implements HomeBlockDto {
}
