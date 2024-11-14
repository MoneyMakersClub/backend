package com.mmc.bookduck.domain.userhome.dto.common;

public record BookWithMemoCardDto(
        Long homeCardId,
        Long cardIndex,
        Long resourceId1,
        Long resourceId2,
        String imgPath1,
        String imgPath2,
        String textType,
        String text1,
        String text2
) implements HomeCardDto {
}