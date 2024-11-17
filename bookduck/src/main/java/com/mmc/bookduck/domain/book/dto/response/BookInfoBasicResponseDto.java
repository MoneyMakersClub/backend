package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.dto.common.BookUnitDto;

public record BookInfoBasicResponseDto(
        BookUnitDto bookInfoBasicDto,
        Double ratingAverage,
        Long oneLineId,
        String myOneLine,
        BookInfoDetailDto bookInfoDetailDto
) {
    public static BookInfoBasicResponseDto from(BookUnitDto basicDto, Double ratingAverage, Long oneLineId, String myOneLine, BookInfoDetailDto dto){
        return new BookInfoBasicResponseDto(
                basicDto,
                ratingAverage,
                oneLineId,
                myOneLine,
                dto
        );
    }
    public static BookInfoBasicResponseDto from(BookUnitDto basicDto, Double ratingAverage, BookInfoDetailDto dto){
        return new BookInfoBasicResponseDto(
                basicDto,
                ratingAverage,
                null,
                null,
                dto
        );
    }

    public static BookInfoBasicResponseDto from(BookUnitDto basicDto, BookInfoDetailDto dto){
        return new BookInfoBasicResponseDto(
                basicDto,
                null,
                null,
                null,
                dto
        );
    }
}