package com.mmc.bookduck.domain.folder.dto.response;

import com.mmc.bookduck.domain.folder.dto.common.FolderBookUnitDto;
import com.mmc.bookduck.domain.folder.entity.Folder;

import java.util.List;

public record FolderBookListResponseDto(String folderName, int folderBookCount, List<FolderBookUnitDto> folderBookList) {
    public FolderBookListResponseDto(Folder folder, List<FolderBookUnitDto> folderBookList) {
        this(folder.getFolderName(), folderBookList.size(), folderBookList);
    }
}
