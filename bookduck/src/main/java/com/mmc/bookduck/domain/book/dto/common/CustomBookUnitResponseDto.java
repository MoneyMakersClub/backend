package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import jakarta.validation.constraints.NotNull;


public record CustomBookUnitResponseDto(
        @NotNull String title,
        String author,
        String imgPath,
        Long bookinfoId
) {
    public static CustomBookUnitResponseDto from(BookInfo bookInfo){
        return new CustomBookUnitResponseDto(
                bookInfo.getTitle(),
                bookInfo.getAuthor(),
                bookInfo.getImgPath(),
                bookInfo.getBookInfoId()
        );
    }
}

