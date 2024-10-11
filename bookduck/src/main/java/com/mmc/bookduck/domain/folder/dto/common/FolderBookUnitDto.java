package com.mmc.bookduck.domain.folder.dto.common;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.folder.entity.FolderBook;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FolderBookUnitDto {
    private Long folderBookId;
    private Long userBookId;
    private String title;
    private String author;
    private String imgPath;
    private ReadStatus readStatus;

    public static FolderBookUnitDto from(FolderBook folderBook) {
        return new FolderBookUnitDto(
                folderBook.getFolderBookId(),
                folderBook.getUserBook().getUserBookId(),
                folderBook.getUserBook().getBookInfo().getTitle(),
                folderBook.getUserBook().getBookInfo().getAuthor(),
                folderBook.getUserBook().getBookInfo().getImgPath(),
                folderBook.getUserBook().getReadStatus()
        );
    }
}
