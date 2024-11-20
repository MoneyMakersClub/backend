package com.mmc.bookduck.domain.homecard.dto.common;

import com.mmc.bookduck.domain.oneline.entity.OneLine;

public record OneLineRatingWithBookInfoUnitDto (
        Long oneLineId,
        String oneLineContent,
        double rating,
        String title,
        String author
) {
    public OneLineRatingWithBookInfoUnitDto(OneLine oneLine) {
        this(
                oneLine.getOneLineId(),
                oneLine.getOneLineContent(),
                oneLine.getUserBook().getRating(),
                oneLine.getUserBook().getBookInfo().getTitle(),
                oneLine.getUserBook().getBookInfo().getAuthor()
        );
    }
}
