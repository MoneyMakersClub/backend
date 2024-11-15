package com.mmc.bookduck.domain.userhome.dto.request;

import com.mmc.bookduck.domain.userhome.dto.common.HomeCardUpdateUnitDto;

import java.util.List;

public record ReadingSpaceUpdateRequestDto(
        List<HomeCardUpdateUnitDto> updatedCardList
) {
}