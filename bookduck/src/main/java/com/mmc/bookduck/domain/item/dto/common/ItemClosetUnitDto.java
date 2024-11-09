package com.mmc.bookduck.domain.item.dto.common;

import com.mmc.bookduck.domain.item.entity.Item;
import com.mmc.bookduck.domain.item.entity.ItemType;

public record ItemClosetUnitDto (
        ItemType itemType,
        Long itemId,
        Boolean isOwned,
        Boolean isEquipped
) {
    public static ItemClosetUnitDto from(Item item, Boolean isOwned, Boolean isEquipped){
        return new ItemClosetUnitDto(
                item.getItemType(),
                item.getItemId(),
                isOwned,
                isEquipped
        );
    }
}
