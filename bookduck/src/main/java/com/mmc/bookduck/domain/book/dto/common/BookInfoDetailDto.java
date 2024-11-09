package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.UserBook;

import java.util.List;

public record BookInfoDetailDto(
        String publisher,
        String publishedDate,
        String description,
        Long pageCount,
        List<String> category,
        Long genreId,
        String koreanGenreName,
        String language
) {

    public static BookInfoDetailDto from(BookInfo bookInfo, String koreanGenreName) {
        return new BookInfoDetailDto(
                bookInfo.getPublisher(),
                bookInfo.getPublishDate(),
                bookInfo.getDescription(),
                bookInfo.getPageCount(),
                null,
                bookInfo.getGenre().getGenreId(),
                koreanGenreName,
                bookInfo.getLanguage()
        );
    }
}
