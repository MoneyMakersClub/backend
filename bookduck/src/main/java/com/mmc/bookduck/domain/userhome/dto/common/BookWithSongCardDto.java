package com.mmc.bookduck.domain.userhome.dto.common;

import com.mmc.bookduck.domain.userhome.entity.CardType;

public record BookWithSongCardDto(
        Long homeCardId,
        Long cardIndex,
        CardType cardType,
        String imgPath1,
        String imgPath2,
        String text1,
        String text2,
        String nickname
) implements HomeCardDto {
}