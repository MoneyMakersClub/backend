package com.mmc.bookduck.domain.oneline.dto.response;

import com.mmc.bookduck.domain.oneline.entity.OneLine;

public record OneLineRatingUnitDto(
        String oneLineContent,
        double rating,
        int oneLineLikes,
        String userNickName
) {
    public static OneLineRatingUnitDto fromEntity(OneLine oneLine) {
        return new OneLineRatingUnitDto(
                oneLine.getOneLineContent(),
                oneLine.getUserBook().getRating(),
                oneLine.getOneLineLikes().size(),
                oneLine.getUser().getNickname()
        );
    }
}