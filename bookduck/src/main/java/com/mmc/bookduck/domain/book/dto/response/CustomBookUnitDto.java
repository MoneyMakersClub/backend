package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.MyRatingOneLineReadStatusDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import jakarta.validation.constraints.NotNull;


public record CustomBookUnitDto(
        @NotNull String title,
        String author,
        String imgPath,
        Long bookinfoId,
        Double myRating,
        ReadStatus readStatus
) {
    public static CustomBookUnitDto from(BookInfo bookInfo, MyRatingOneLineReadStatusDto dto){
        return new CustomBookUnitDto(
                bookInfo.getTitle(),
                bookInfo.getAuthor(),
                bookInfo.getImgPath(),
                bookInfo.getBookInfoId(),
                dto.myRating(),
                dto.readStatus()
        );
    }
}

