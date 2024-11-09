package com.mmc.bookduck.domain.item.dto.response;

import com.mmc.bookduck.domain.item.dto.common.ItemClosetUnitDto;
import com.mmc.bookduck.domain.item.entity.ItemType;

import java.util.List;
import java.util.Map;

public record UserItemClosetResponseDto (
    Map<ItemType, Long> userItemEquipped,
    List<ItemClosetUnitDto> itemList
) {
}