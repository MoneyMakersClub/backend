package com.mmc.bookduck.domain.folder.dto.request;

import com.mmc.bookduck.domain.folder.dto.common.FolderBookOrderUnitDto;
import java.util.List;

public record FolderBookOrderRequestDto(List<FolderBookOrderUnitDto> folderBooksOrder) {
}
