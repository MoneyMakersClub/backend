package com.mmc.bookduck.domain.item.dto.response;

import com.mmc.bookduck.domain.item.dto.common.ItemClosetUnitDto;
import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;

import java.util.List;

public record UserItemClosetResponseDto (
        List<ItemEquippedUnitDto> userItemEquipped,
        List<ItemClosetUnitDto> itemList
) {
}