package com.mmc.bookduck.domain.item.dto.request;

import com.mmc.bookduck.domain.item.entity.ItemType;

import java.util.Map;

public record UserItemUpdateRequestDto(
        Map<ItemType, Long> equippedUserItemIdList
) {
}

