package com.mmc.bookduck.domain.userhome.dto.response;

import com.mmc.bookduck.domain.userhome.dto.common.HomeBlockDto;

import java.util.List;

public record UserReadingSpaceResponseDto(
        List<HomeBlockDto> homeCardList
) {
}
