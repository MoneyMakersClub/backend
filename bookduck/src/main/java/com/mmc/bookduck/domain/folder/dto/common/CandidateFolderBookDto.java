package com.mmc.bookduck.domain.folder.dto.common;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;

public record CandidateFolderBookDto(
        Long userBookId,
        String title,
        String author,
        String imgPath,
        double rating,
        ReadStatus readStatus,
        boolean isCustom
) {
    public CandidateFolderBookDto(UserBook userBook, boolean isCustom) {
        this(
                userBook.getUserBookId(),
                userBook.getBookInfo().getTitle(),
                userBook.getBookInfo().getAuthor(),
                userBook.getBookInfo().getImgPath(),
                userBook.getRating(),
                userBook.getReadStatus(),
                isCustom
        );
    }
}