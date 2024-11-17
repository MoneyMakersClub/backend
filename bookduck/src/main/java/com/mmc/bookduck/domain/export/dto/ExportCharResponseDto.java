package com.mmc.bookduck.domain.export.dto;

import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;
import com.mmc.bookduck.domain.user.dto.response.UserKeywordResponseDto;

import java.util.List;

public record ExportCharResponseDto(
        String nickname,
        String duckTitle,
        UserKeywordResponseDto keywords,
        List<ItemEquippedUnitDto> userItemEquipped
) {
}
