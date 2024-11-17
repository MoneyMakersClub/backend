package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;


public record MyRatingOneLineReadStatusDto(Long userbookId, Double myRating, Long oneLineId, String myOneLine, ReadStatus readStatus) {

    public static MyRatingOneLineReadStatusDto defaultInstance() {
        return new MyRatingOneLineReadStatusDto(null, 0.0, null, null, null);
    }

    public static MyRatingOneLineReadStatusDto from(UserBook userBook){
        return new MyRatingOneLineReadStatusDto(
                userBook.getUserBookId(),
                userBook.getRating(),
                null,
                null,
                userBook.getReadStatus()
        );
    }

    public static MyRatingOneLineReadStatusDto from(UserBook userBook, OneLine oneLine){
        return new MyRatingOneLineReadStatusDto(
                userBook.getUserBookId(),
                userBook.getRating(),
                oneLine.getOneLineId(),
                oneLine.getOneLineContent(),
                userBook.getReadStatus()
        );
    }
}
