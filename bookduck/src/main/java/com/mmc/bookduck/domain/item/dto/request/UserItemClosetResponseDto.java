package com.mmc.bookduck.domain.item.dto.request;

import com.mmc.bookduck.domain.item.dto.common.ItemClosetUnitDto;
import com.mmc.bookduck.domain.item.entity.ItemType;

import java.util.List;
import java.util.Map;

public record UserItemClosetResponseDto (
    Map<ItemType, Long> equippedItems,
    List<ItemClosetUnitDto> itemList
) {
}