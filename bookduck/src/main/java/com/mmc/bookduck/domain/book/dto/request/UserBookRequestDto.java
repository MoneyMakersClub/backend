package com.mmc.bookduck.domain.book.dto.request;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;


import jakarta.validation.constraints.NotNull;
import java.util.List;


public record UserBookRequestDto(
        @NotBlank(message = "title은 필수입니다. title은 공백일 수 없습니다.") String title,
        @NotNull(message = "author은 필수입니다.") String author,
        @NotBlank(message = "readStatus는 필수입니다.") String readStatus,
        String publisher,
        String publishDate,
        String description,
        List<String> category,
        @NotNull(message = "genreId는 필수입니다.") Long genreId,
        Long pageCount,
        String imgPath,
        String language,
        @NotNull(message = "providerId는 필수입니다.") String providerId
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