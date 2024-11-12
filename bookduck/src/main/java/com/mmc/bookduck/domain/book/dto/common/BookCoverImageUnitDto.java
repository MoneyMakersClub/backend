package com.mmc.bookduck.domain.book.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mmc.bookduck.domain.book.entity.UserBook;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookCoverImageUnitDto (Long userbookId,
                                     Long bookInfoId,
                                     String imgPath,
                                     String title,
                                     String author){
    public static BookCoverImageUnitDto from(UserBook userBook) {
        return new BookCoverImageUnitDto(
                userBook.getUserBookId(),
                null,
                userBook.getBookInfo().getImgPath(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor()
        );
    }
}
