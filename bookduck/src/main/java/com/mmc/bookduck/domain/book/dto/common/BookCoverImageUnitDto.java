package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record BookCoverImageUnitDto (Long bookInfoId,
                                     String imgPath,
                                     String title,
                                     String author){

    public static BookCoverImageUnitDto from(BookInfo bookInfo) {
        return new BookCoverImageUnitDto(
                bookInfo.getBookInfoId(),
                bookInfo.getImgPath(),
                bookInfo.getTitle(),
                bookInfo.getAuthor()
        );
    }
}
