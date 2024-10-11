package com.mmc.bookduck.domain.folder.dto.common;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.folder.entity.Folder;
import com.mmc.bookduck.domain.folder.entity.FolderBook;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderBookCoverListDto {
    private Long folderId;
    private String folderName;
    private List<FolderBookCoverDto> folderBookCoverList;
    private int folderBookCount;

    public static FolderBookCoverListDto from(Folder folder, List<FolderBook> folderBooks) {
        List<FolderBookCoverDto> coverList = new ArrayList<>();

        for (FolderBook book : folderBooks) {
            coverList.add(new FolderBookCoverDto(book.getFolderBookId(), book.getUserBook().getUserBookId(), book.getUserBook().getBookInfo().getImgPath()));
        }

        return new FolderBookCoverListDto(
                folder.getFolderId(),
                folder.getFolderName(),
                coverList,
                coverList.size()
        );
    }
}
@Getter
@AllArgsConstructor
class FolderBookCoverDto {
    private Long folderBookId;
    private Long userBookId;
    private String imgPath;
}

