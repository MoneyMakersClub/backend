package com.mmc.bookduck.domain.oneline.dto.response;

import com.mmc.bookduck.domain.oneline.entity.OneLine;

import java.time.LocalDateTime;

public record OneLineRatingUnitDto(
        String oneLineContent,
        double rating,
        int oneLineLikeCount,
        Boolean isLiked,
        LocalDateTime createdTime,
        Long userId,
        String userNickName
) {
    public static OneLineRatingUnitDto from(OneLine oneLine, Boolean isLiked) {
        return new OneLineRatingUnitDto(
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