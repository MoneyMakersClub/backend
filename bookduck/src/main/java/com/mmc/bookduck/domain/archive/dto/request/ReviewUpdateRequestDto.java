package com.mmc.bookduck.domain.archive.dto.request;

import com.mmc.bookduck.domain.common.Visibility;

public record ReviewUpdateRequestDto(
        String reviewTitle,
        String reviewContent,
        String color,
        Visibility reviewVisibility
) {}
