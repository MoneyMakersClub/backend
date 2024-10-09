package com.mmc.bookduck.domain.book.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserBookRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private List<String> authors;

    private String publisher; // null 가능

    private String publishDate; // null 가능, 연도만 제공될 수 있음

    private String description;

    private List<String> category;

    private Long pageCount;

    private String imgPath;

    private String language;

    @NotBlank
    private String providerId;
}

