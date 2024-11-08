package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.ReadStatus;

public record CustomBookResponseDto(Long bookInfoId, String title, String author, String publisher, Long pageCount, ReadStatus readStatus, Double myRating, String myOneLine){

    public static CustomBookResponseDto from(BookInfo bookInfo, Double myRating, String myOneLine,
                                             ReadStatus readStatus){
        return new CustomBookResponseDto(
                bookInfo.getBookInfoId(),
                bookInfo.getTitle(),
                bookInfo.getAuthor(),
                bookInfo.getPublisher(),
                bookInfo.getPageCount(),
                readStatus,
                myRating,
                myOneLine
                );
    }
}
