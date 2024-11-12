package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.BookInfoDetailDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record BookInfoBasicResponseDto(
        Long bookInfoId,
        Long userbookId,
        Double ratingAverage,
        String myOneLine,
        Double myRating,
        ReadStatus readStatus,
        BookInfoDetailDto bookInfoDetailDto
) {
    public static BookInfoBasicResponseDto from(UserBook userBook, Double ratingAverage, String myOneLine, BookInfoDetailDto dto){
        return new BookInfoBasicResponseDto(
                userBook.getBookInfo().getBookInfoId(),
                userBook.getUserBookId(),
                ratingAverage,
                myOneLine,
                userBook.getRating(),
                userBook.getReadStatus(),
                dto
        );
    }
    public static BookInfoBasicResponseDto from(BookInfo bookInfo, Double ratingAverage, BookInfoDetailDto dto){
        return new BookInfoBasicResponseDto(
                bookInfo.getBookInfoId(),
                null,
                ratingAverage,
                null,
                null,
                null,
                dto
        );
    }
}