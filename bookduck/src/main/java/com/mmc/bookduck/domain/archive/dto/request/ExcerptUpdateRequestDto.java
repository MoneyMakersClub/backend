package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.common.Visibility;

public record ExcerptUpdateRequestDto(
        String excerptContent,
        Long pageNumber,
        Visibility excerptVisibility
) {

}


