package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record UserBookResponseDto(
        Long userBookId,
        String title,
        String author,
        String imgPath,
        ReadStatus readStatus,
        Long bookInfoId
) {
    public static UserBookResponseDto from(UserBook userBook) {
        return new UserBookResponseDto(
                userBook.getUserBookId(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor(),
                userBook.getBookInfo().getImgPath(),
                userBook.getReadStatus(),
                userBook.getBookInfo().getBookInfoId()
        );
    }
}