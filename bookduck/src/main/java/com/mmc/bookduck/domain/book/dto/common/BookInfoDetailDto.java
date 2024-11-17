package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.dto.request.AddUserBookRequestDto;
import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
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

    public BookInfo toEntity(String providerId, AddUserBookRequestDto dto, Genre genre) {
        return BookInfo.builder()
                .providerId(providerId)
                .title(dto.title())
                .author(dto.authors().getFirst())
                .publisher(this.publisher)
                .publishDate(this.publishedDate)
                .description(this.description)
                .category(this.category != null && !this.category.isEmpty() ? this.category.get(0) : null)
                .genre(genre)
                .pageCount(this.pageCount)
                .imgPath(dto.imgPath())
                .language(this.language)
                .build();
    }
}
