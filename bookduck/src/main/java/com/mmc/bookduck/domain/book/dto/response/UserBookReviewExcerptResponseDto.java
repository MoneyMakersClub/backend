package com.mmc.bookduck.domain.book.dto.response;

import com.mmc.bookduck.domain.book.dto.common.ReviewExcerptUnitDto;
import java.util.List;

public record UserBookReviewExcerptResponseDto(Long bookInfoId,
                                               Long userBookId,
                                               List<ReviewExcerptUnitDto> archiveList) {
}
