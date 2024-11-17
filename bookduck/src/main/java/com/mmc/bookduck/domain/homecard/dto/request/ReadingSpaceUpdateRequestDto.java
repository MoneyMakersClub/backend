package com.mmc.bookduck.domain.homecard.dto.request;

import com.mmc.bookduck.domain.homecard.dto.common.HomeCardUpdateUnitDto;

import java.util.List;

public record ReadingSpaceUpdateRequestDto(
        List<HomeCardUpdateUnitDto> updatedCardList
) {
}