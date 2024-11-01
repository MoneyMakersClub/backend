package com.mmc.bookduck.domain.book.dto.common;

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

    public static BookInfoDetailDto from(UserBook userBook, String koreanGenreName) {
        return new BookInfoDetailDto(
                userBook.getBookInfo().getPublisher(),
                userBook.getBookInfo().getPublishDate(),
                userBook.getBookInfo().getDescription(),
                userBook.getBookInfo().getPageCount(),
                null,
                userBook.getBookInfo().getGenre().getGenreId(),
                koreanGenreName,
                userBook.getBookInfo().getLanguage()
        );
    }
}
