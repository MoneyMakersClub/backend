package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import lombok.NoArgsConstructor;


public record MyRatingOneLineReadStatusDto(Long userbookId, Double myRating, String myOneLine, ReadStatus readStatus) {

    public static MyRatingOneLineReadStatusDto defaultInstance() {
        return new MyRatingOneLineReadStatusDto(null, null, null, null);
    }

    public static MyRatingOneLineReadStatusDto from(UserBook userBook){
        return new MyRatingOneLineReadStatusDto(
                userBook.getUserBookId(),
                userBook.getRating(),
                null,
                userBook.getReadStatus()
        );
    }

    public static MyRatingOneLineReadStatusDto from(UserBook userBook, String oneLine){
        return new MyRatingOneLineReadStatusDto(
                userBook.getUserBookId(),
                userBook.getRating(),
                oneLine,
                userBook.getReadStatus()
        );
    }
}
