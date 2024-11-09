package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record CustomBookResponseDto(Long bookInfoId, Long userBookId, String title, String author, String publisher, Long pageCount, ReadStatus readStatus, Double myRating, String myOneLine){

    public static CustomBookResponseDto from(UserBook userBook, Double myRating, String myOneLine){
        return new CustomBookResponseDto(
                userBook.getBookInfo().getBookInfoId(),
                userBook.getUserBookId(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor(),
                userBook.getBookInfo().getPublisher(),
                userBook.getBookInfo().getPageCount(),
                userBook.getReadStatus(),
                myRating,
                myOneLine
                );
    }
}
