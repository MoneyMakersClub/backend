package com.mmc.bookduck.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

public record UserKeywordResponseDto(
        List<String> keywords
) {
    @JsonValue
    public List<String> toJson() {
        return keywords;
    }

    public static UserKeywordResponseDto from(List<String> keywords, int limit) {
        return new UserKeywordResponseDto(
                keywords.stream()
                        .limit(limit)
                        .toList()
        );
    }
}
