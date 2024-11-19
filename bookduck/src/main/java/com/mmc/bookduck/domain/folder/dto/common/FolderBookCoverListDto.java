package com.mmc.bookduck.domain.folder.dto.common;

import com.mmc.bookduck.domain.folder.entity.Folder;
import com.mmc.bookduck.domain.folder.entity.FolderBook;

import java.util.ArrayList;
import java.util.List;

public record FolderBookCoverListDto(
        Long folderId,
        String folderName,
        List<FolderBookCoverDto> folderBookCoverList,
        int folderBookCount
) {
    public static FolderBookCoverListDto from(Folder folder, List<FolderBookCoverDto> coverList) {
        return new FolderBookCoverListDto(
                folder.getFolderId(),
                folder.getFolderName(),
                coverList,
                coverList.size()
        );
    }
}
