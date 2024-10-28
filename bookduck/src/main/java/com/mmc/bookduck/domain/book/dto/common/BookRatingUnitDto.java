package com.mmc.bookduck.domain.book.dto.common;

public record BookRatingUnitDto(
        String nickname,
        double rating,
        String oneLine
) {
}