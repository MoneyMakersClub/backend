package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookUnitParseDto;
import com.mmc.bookduck.domain.book.dto.common.MyRatingOneLineDto;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Setter;

public record BookUnitResponseDto(
        @NotNull String title,
        List<String> author,
        String imgPath,
        Double myRating,
        String myOneLine,
        String providerId
) {
    public static BookUnitResponseDto from(BookUnitParseDto infoDto, MyRatingOneLineDto ratingDto){
        return new BookUnitResponseDto(
                infoDto.title(),
                infoDto.author(),
                infoDto.imgPath(),
                ratingDto.myRating(),
                ratingDto.myOneLine(),
                infoDto.providerId()
        );
    }
    public static BookUnitResponseDto from(BookUnitParseDto infoDto){
        return new BookUnitResponseDto(
                infoDto.title(),
                infoDto.author(),
                infoDto.imgPath(),
                null,
                null,
                infoDto.providerId()
        );
    }
}