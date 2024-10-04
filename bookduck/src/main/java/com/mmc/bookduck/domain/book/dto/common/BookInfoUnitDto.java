package com.mmc.bookduck.domain.book.dto.common;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoUnitDto {
    @NotNull
    private String title;

    private List<String> author;

    private String publisher;

    private Long publishedYear;

    private String imgPath;

    private String providerId;

    @Builder
    public BookInfoUnitDto(String title, List<String> author, String publisher,
                           Long publishedYear, String imgPath, String providerId) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishedYear = publishedYear;
        this.imgPath = imgPath;
        this.providerId = providerId;
    }
}
