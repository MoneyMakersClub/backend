package com.mmc.bookduck.domain.user.dto.response;

import java.util.List;

public record UserKeywordResponseDto(
        List<String> keywords
) {
    public static UserKeywordResponseDto from(List<String> keywords, int limit) {
        return new UserKeywordResponseDto(
                keywords.stream()
                        .limit(limit)
                        .toList()
        );
    }
}
