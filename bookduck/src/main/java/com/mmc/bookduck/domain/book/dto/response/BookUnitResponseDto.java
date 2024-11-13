package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookUnitParseDto;
import com.mmc.bookduck.domain.book.dto.common.MyRatingOneLineReadStatusDto;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BookUnitResponseDto(
        String providerId,
        Long bookInfoId,
        @NotNull String title,
        List<String> author,
        String imgPath,
        Double myRating,
        ReadStatus readStatus
) {
    public static BookUnitResponseDto from(BookUnitParseDto infoDto, MyRatingOneLineReadStatusDto ratingDto, Long bookInfoId){
        return new BookUnitResponseDto(
                infoDto.providerId(),
                bookInfoId,
                infoDto.title(),
                infoDto.author(),
                infoDto.imgPath(),
                ratingDto.myRating(),
                ratingDto.readStatus()
        );
    }
    public static BookUnitResponseDto from(BookUnitParseDto infoDto){
        return new BookUnitResponseDto(
                infoDto.providerId(),
                null,
                infoDto.title(),
                infoDto.author(),
                infoDto.imgPath(),
                0.0,
                null
        );
    }
}