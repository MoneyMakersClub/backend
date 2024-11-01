package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.onelinerating.entity.OneLineRating;

public record BookRatingUnitDto(
        String nickname,
        double rating,
        String oneLine
) {
    public static BookRatingUnitDto from(OneLineRating oneLineRating) {
        return new BookRatingUnitDto(
                oneLineRating.getUser().getNickname(),
                oneLineRating.getRating(),
                oneLineRating.getOneLineContent()
        );
    }
}