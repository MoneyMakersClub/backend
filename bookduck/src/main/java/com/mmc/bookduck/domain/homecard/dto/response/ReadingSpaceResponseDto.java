package com.mmc.bookduck.domain.homecard.dto.response;

import com.mmc.bookduck.domain.homecard.dto.common.HomeCardDto;

import java.util.List;

public record ReadingSpaceResponseDto(
        List<HomeCardDto> cardList
) {
}
