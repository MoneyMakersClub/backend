package com.mmc.bookduck.domain.book.dto.common;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdditionalBookInfoDto {
    private String publisher;
    private String publishedDate;
    private String description;
    private Long pageCount;
    private List<String> category;
    private String language;

    @Builder
    public AdditionalBookInfoDto(String publisher, String publishedDate,
                                 String description, Long pageCount, List<String> category, String language){
        this.publishedDate = publishedDate;
        this.publisher = publisher;
        this.description = description;
        this.pageCount = pageCount;
        this.category = category;
        this.language = language;
    }
}
