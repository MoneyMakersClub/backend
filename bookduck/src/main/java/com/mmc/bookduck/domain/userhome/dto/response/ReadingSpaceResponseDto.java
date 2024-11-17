package com.mmc.bookduck.domain.userhome.dto.response;

import com.mmc.bookduck.domain.userhome.dto.common.HomeCardDto;

import java.util.List;

public record ReadingSpaceResponseDto(
        List<HomeCardDto> cardList
) {
}
