package com.mmc.bookduck.domain.user.dto.common;

public record MostReadGenreUnitDto(
        String genreName,
        long bookCount
) {
}
