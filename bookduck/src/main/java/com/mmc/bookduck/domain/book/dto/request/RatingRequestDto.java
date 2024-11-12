package com.mmc.bookduck.domain.book.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record RatingRequestDto(@NotNull
                               @Min(value = 1, message = "최소 별점은 1점입니다.")
                               @Max(value = 5, message = "최대 별점은 5점입니다.") double rating){
}
