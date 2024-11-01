package com.mmc.bookduck.domain.book.dto.request;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


public record UserBookRequestDto(
        @NotBlank String title,
        @NotBlank List<String> authors,
        @NotBlank String readStatus,
        String publisher,
        String publishDate,
        String description,
        List<String> category,
        @NotBlank Long genreId,
        Long pageCount,
        String imgPath,
        String language,
        @NotBlank String providerId
) {

    public UserBook toEntity(User user, BookInfo bookInfo, ReadStatus readStatus) {
        return UserBook.builder()
                .readStatus(readStatus)
                .user(user)
                .bookInfo(bookInfo)
                .build();
    }


    public BookInfo toEntity(String author, Genre genre) {
        return BookInfo.builder()
                .providerId(this.providerId)
                .title(this.title)
                .author(author)
                .publisher(this.publisher)
                .publishDate(this.publishDate)
                .description(this.description)
                .category(this.category != null && !this.category.isEmpty() ? this.category.get(0) : null)
                .genre(genre)
                .pageCount(this.pageCount)
                .imgPath(this.imgPath)
                .language(this.language)
                .build();
    }
}