package com.mmc.bookduck.domain.item.dto.common;

import com.mmc.bookduck.domain.item.entity.Item;
import com.mmc.bookduck.domain.item.entity.ItemType;
import com.mmc.bookduck.domain.item.entity.UserItem;

public record ItemClosetUnitDto (
        Long itemId,
        ItemType itemType,
        String itemName,
        Boolean isOwned,
        Long userItemId,
        Boolean isEquipped
) {
    public static ItemClosetUnitDto from(Item item, UserItem userItem){
        Long userItemId = (userItem != null) ? userItem.getUserItemId() : null;
        Boolean isOwned = (userItem != null);
        Boolean isEquipped = (userItem != null && userItem.isEquipped());

        return new ItemClosetUnitDto(
                item.getItemId(),
                item.getItemType(),
                item.getItemName(),
                isOwned,
                userItemId,
                isEquipped
        );
    }
}
