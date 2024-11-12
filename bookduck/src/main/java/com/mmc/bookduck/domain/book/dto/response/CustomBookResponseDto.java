package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record CustomBookResponseDto(boolean isMine, Long bookInfoId, Long userBookId, String title, String author, String imgPath, String publisher, Long pageCount, ReadStatus readStatus, Double myRating, String myOneLine){

    public static CustomBookResponseDto from(UserBook userBook, Double myRating, String myOneLine, boolean isMine){
        return new CustomBookResponseDto(
                isMine,
                userBook.getBookInfo().getBookInfoId(),
                userBook.getUserBookId(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor(),
                userBook.getBookInfo().getImgPath(),
                userBook.getBookInfo().getPublisher(),
                userBook.getBookInfo().getPageCount(),
                userBook.getReadStatus(),
                myRating,
                myOneLine
                );
    }
    public static CustomBookResponseDto from(UserBook userBook, boolean isMine){
        return new CustomBookResponseDto(
                isMine,
                userBook.getBookInfo().getBookInfoId(),
                userBook.getUserBookId(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor(),
                userBook.getBookInfo().getImgPath(),
                userBook.getBookInfo().getPublisher(),
                userBook.getBookInfo().getPageCount(),
                null,
                null,
                null
        );
    }
}
