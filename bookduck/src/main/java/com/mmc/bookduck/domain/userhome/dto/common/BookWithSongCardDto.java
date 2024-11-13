package com.mmc.bookduck.domain.userhome.dto.common;

public record BookWithSongCardDto(
        Long homeCardId,
        Long bookInfoId1,
        Long bookInfoId2,
        String imgPath1,
        String imgPath2,
        String textType,
        String text1,
        String text2,
        String nickname
) implements HomeCardDto {

}