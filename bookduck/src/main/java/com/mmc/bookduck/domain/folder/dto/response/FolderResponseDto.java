package com.mmc.bookduck.domain.folder.dto.response;

import com.mmc.bookduck.domain.folder.entity.Folder;

public record FolderResponseDto(Long folderId, String folderName, Long userId) {
    public FolderResponseDto(Folder savedFolder) {
        this(savedFolder.getFolderId(), savedFolder.getFolderName(), savedFolder.getUser().getUserId());
    }
}
