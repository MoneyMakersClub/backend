package com.mmc.bookduck.domain.folder.dto.response;

import com.mmc.bookduck.domain.folder.dto.common.FolderBookUnitDto;
import com.mmc.bookduck.domain.folder.entity.Folder;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderBookListResponseDto {
    private Long folderId;
    private String folderName;
    private int folderBookCount;
    private List<FolderBookUnitDto> folderBookList;


    public FolderBookListResponseDto(Folder folder, List<FolderBookUnitDto> folderBookList) {
        this.folderId = folder.getFolderId();
        this.folderName = folder.getFolderName();
        this.folderBookList = folderBookList;
        this.folderBookCount = folderBookList.size();
    }
}
