package com.mmc.bookduck.domain.book.dto.request;

import com.mmc.bookduck.domain.book.entity.UserBook;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDto(@NotNull double rating){
}
