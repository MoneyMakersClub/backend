package com.mmc.bookduck.domain.book.dto.common;

import com.mmc.bookduck.domain.book.entity.ReadStatus;

public record MyRatingOneLineReadStatusDto(Double myRating, String myOneLine, ReadStatus readStatus) {
}
