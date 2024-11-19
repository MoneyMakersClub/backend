package com.mmc.bookduck.domain.folder.dto.common;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.folder.entity.FolderBook;

public record FolderBookUnitDto(
        Long folderBookId,
        Long bookinfoId,
        Long userBookId,
        String title,
        String author,
        String imgPath,
        double rating,
        ReadStatus readStatus,
        int folderBookOrder,
        boolean isCustom
) {
    public FolderBookUnitDto(FolderBook folderBook, boolean isCustom) {
        this(
                folderBook.getFolderBookId(),
                folderBook.getUserBook().getBookInfo().getBookInfoId(),
                folderBook.getUserBook().getUserBookId(),
                folderBook.getUserBook().getBookInfo().getTitle(),
                folderBook.getUserBook().getBookInfo().getAuthor(),
                folderBook.getUserBook().getBookInfo().getImgPath(),
                folderBook.getUserBook().getRating(),
                folderBook.getUserBook().getReadStatus(),
                folderBook.getBookOrder(),
                isCustom
        );
    }
}
