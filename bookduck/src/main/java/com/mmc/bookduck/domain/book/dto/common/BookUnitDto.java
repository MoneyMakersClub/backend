package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

public record BookUnitDto(
        Long bookInfoId,
        Long userbookId,
        @NotNull String title,
        List<String> author,
        String imgPath,
        Double myRating,
        ReadStatus readStatus
) {
    public static BookUnitDto from(BookUnitParseDto infoDto, MyRatingOneLineReadStatusDto ratingDto, Long bookInfoId){
        return new BookUnitDto(
                bookInfoId,
                ratingDto.userbookId(),
                infoDto.title(),
                infoDto.author(),
                infoDto.imgPath(),
                ratingDto.myRating(),
                ratingDto.readStatus()
        );
    }
    public static BookUnitDto from(BookUnitParseDto infoDto){
        return new BookUnitDto(
                null,
                null,
                infoDto.title(),
                infoDto.author(),
                infoDto.imgPath(),
                0.0,
                null
        );
    }
    public static BookUnitDto from(BookInfo bookInfo, MyRatingOneLineReadStatusDto ratingDto){
        return new BookUnitDto(
                bookInfo.getBookInfoId(),
                ratingDto.userbookId(),
                bookInfo.getTitle(),
                Collections.singletonList(bookInfo.getAuthor()),
                bookInfo.getImgPath(),
                ratingDto.myRating(),
                ratingDto.readStatus()
        );
    }
}