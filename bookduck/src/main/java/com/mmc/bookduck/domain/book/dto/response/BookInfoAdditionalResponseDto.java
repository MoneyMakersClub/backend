package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookRatingUnitDto;

import java.util.List;

public record BookInfoAdditionalResponseDto(
        List<BookRatingUnitDto> list
) {
}