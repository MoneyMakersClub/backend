package com.mmc.bookduck.domain.folder.dto.common;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record CandidateFolderBookDto(
        Long userBookId,
        String title,
        String author,
        String imgPath,
        ReadStatus readStatus
) {
    public static CandidateFolderBookDto from(UserBook userBook) {
        return new CandidateFolderBookDto(
                userBook.getUserBookId(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor(),
                userBook.getBookInfo().getImgPath(),
                userBook.getReadStatus()
        );
    }
}