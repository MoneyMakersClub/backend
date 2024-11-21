package com.mmc.bookduck.domain.user.dto.common;

import com.mmc.bookduck.domain.book.entity.GenreName;

public record MostReadGenreUnitDto(
        GenreName genreName,
        long bookCount
) {
}
