package com.mmc.bookduck.domain.folder.dto.response;

import com.mmc.bookduck.domain.folder.dto.common.FolderBookCoverListDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AllFolderListResponseDto {
    private int folderCount;
    private List<FolderBookCoverListDto> allFolderList;

    public AllFolderListResponseDto(List<FolderBookCoverListDto> allFolderList){
        this.allFolderList = allFolderList;
        this.folderCount = allFolderList.size();
    }
}
