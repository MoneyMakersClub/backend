package com.mmc.bookduck.domain.item.dto.common;

import com.mmc.bookduck.domain.item.entity.ItemType;

import java.util.Map;

public record UserItemEquippedDto(
        Map<ItemType, Long> userItemEquipped
) {
}
