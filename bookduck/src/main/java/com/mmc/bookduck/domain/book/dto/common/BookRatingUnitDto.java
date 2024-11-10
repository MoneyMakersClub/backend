package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.oneline.entity.OneLine;

public record BookRatingUnitDto(
        String nickname,
//        double rating,
        String oneLine
) {
    public static BookRatingUnitDto from(OneLine oneLine) {
        return new BookRatingUnitDto(
                oneLine.getUser().getNickname(),
//                oneLine.getRating(),
                oneLine.getOneLineContent()
        );
    }
}