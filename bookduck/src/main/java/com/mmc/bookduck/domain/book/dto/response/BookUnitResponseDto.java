package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookUnitDto;
import com.mmc.bookduck.domain.book.dto.common.BookUnitParseDto;
import com.mmc.bookduck.domain.book.dto.common.MyRatingOneLineReadStatusDto;
public record BookUnitResponseDto (
        String providerId,
        BookUnitDto bookUnitDto
){
    public static BookUnitResponseDto from(BookUnitParseDto infoDto, MyRatingOneLineReadStatusDto ratingDto, Long bookInfoId){
        return new BookUnitResponseDto(
                infoDto.providerId(),
                BookUnitDto.from(infoDto, ratingDto, bookInfoId)
        );
    }
    public static BookUnitResponseDto from(BookUnitParseDto infoDto){
        return new BookUnitResponseDto(
                infoDto.providerId(),
                BookUnitDto.from(infoDto)
        );
    }
}
