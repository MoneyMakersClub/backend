package com.mmc.bookduck.domain.item.dto.response;

import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;

import java.util.List;

public record UserItemEquippedResponseDto(
        List<ItemEquippedUnitDto> userItemEquipped
) {
}
