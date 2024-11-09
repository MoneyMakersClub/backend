package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import jakarta.validation.constraints.NotNull;


public record CustomBookUnitDto(
        Long bookinfoId,
        @NotNull String title,
        String author,
        String imgPath,
        Double myRating,
        ReadStatus readStatus
) {
    public static CustomBookUnitDto from(BookInfo bookInfo, MyRatingOneLineReadStatusDto dto){
        return new CustomBookUnitDto(
                bookInfo.getBookInfoId(),
                bookInfo.getTitle(),
                bookInfo.getAuthor(),
                bookInfo.getImgPath(),
                dto.myRating(),
                dto.readStatus()
        );
    }
}

