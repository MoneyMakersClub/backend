package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;

public record BookRatingUnitDto(
        String nickname,
        double rating,
        String oneLine
) {
    public static BookRatingUnitDto from(OneLine oneLine, UserBook userBook) {
        return new BookRatingUnitDto(
                oneLine.getUser().getNickname(),
                userBook.getRating(),
                oneLine.getOneLineContent()
        );
    }
}