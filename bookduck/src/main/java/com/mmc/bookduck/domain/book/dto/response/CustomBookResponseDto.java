package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record CustomBookResponseDto(boolean isMine, Long bookInfoId, Long userBookId, String title, String author, String imgPath, ReadStatus readStatus, Double myRating,Long oneLineId, String myOneLine){

    public static CustomBookResponseDto from(UserBook userBook, Double myRating, Long oneLineId, String myOneLine, boolean isMine){
        return new CustomBookResponseDto(
                isMine,
                userBook.getBookInfo().getBookInfoId(),
                userBook.getUserBookId(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor(),
                userBook.getBookInfo().getImgPath(),
                userBook.getReadStatus(),
                myRating,
                oneLineId,
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
                null,
                null,
                null,
                null
        );
    }
}
