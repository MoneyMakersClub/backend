package com.mmc.bookduck.domain.folder.dto.response;

import com.mmc.bookduck.domain.folder.entity.Folder;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderResponseDto {
    private Long folderId;
    private String folderName;
    private Long userId;

    public static FolderResponseDto from(Folder savedFolder) {
        return new FolderResponseDto(savedFolder.getFolderId(), savedFolder.getFolderName(), savedFolder.getUser().getUserId());
    }
}
