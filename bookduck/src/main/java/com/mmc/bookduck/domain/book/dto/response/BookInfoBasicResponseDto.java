package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.entity.ReadStatus;

public record BookInfoBasicResponseDto(
        Double ratingAverage,
        String myOneLine,
        Double myRating,
        ReadStatus readStatus,
        BookInfoDetailDto bookInfoDetailDto
) {
}