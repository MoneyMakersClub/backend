package com.mmc.bookduck.domain.oneline.dto.response;

import com.mmc.bookduck.domain.oneline.entity.OneLine;

import java.time.LocalDateTime;

public record OneLineRatingUnitDto(
        Long oneLineId,
        String oneLineContent,
        double rating,
        int oneLineLikeCount,
        Boolean isLiked,
        LocalDateTime createdTime,
        Long userId,
        String userNickname
) {
    public OneLineRatingUnitDto(OneLine oneLine, Boolean isLiked) {
        this(
                oneLine.getOneLineId(),
                oneLine.getOneLineContent(),
                oneLine.getUserBook().getRating(),
                oneLine.getOneLineLikes().size(),
                isLiked,
                oneLine.getCreatedTime(),
                oneLine.getUser().getUserId(),
                oneLine.getUser().getNickname()
        );
    }
}