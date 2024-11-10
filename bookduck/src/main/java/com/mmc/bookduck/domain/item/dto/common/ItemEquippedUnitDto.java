package com.mmc.bookduck.domain.item.dto.common;

import com.mmc.bookduck.domain.item.entity.ItemType;

public record ItemEquippedUnitDto(
    ItemType itemType,
    String itemName
) {
}