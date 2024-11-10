package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.ReviewExcerptUnitDto;
import java.util.List;

public record UserBookReviewExcerptResponseDto(Long userbookId,
                                               List<ReviewExcerptUnitDto> archiveList) {
}
