package com.mmc.bookduck.domain.homecard.dto.common;

import com.mmc.bookduck.domain.homecard.entity.CardType;

public record BookWithSongCardDto(
        Long cardId,
        Long cardIndex,
        CardType cardType,
        String imgPath1,
        String imgPath2,
        String text1,
        String text2,
        String text3,
        String nickname
) implements HomeCardDto {
}