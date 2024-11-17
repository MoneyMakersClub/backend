package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record AddUserBookResponseDto(Long bookInfoId, Long userBookId, ReadStatus readStatus
){
    public static AddUserBookResponseDto from(UserBook userBook){
        return new AddUserBookResponseDto(
                userBook.getBookInfo().getBookInfoId(),
                userBook.getUserBookId(),
                userBook.getReadStatus()
        );
    }
}
