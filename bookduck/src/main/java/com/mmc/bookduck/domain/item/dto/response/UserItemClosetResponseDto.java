package com.mmc.bookduck.domain.item.dto.response;

import com.mmc.bookduck.domain.item.dto.common.ItemClosetUnitDto;

import java.util.List;

public record UserItemClosetResponseDto (
        List<ItemClosetUnitDto> itemList
) {
}