package com.mmc.bookduck.domain.archive.dto.common;

import com.mmc.bookduck.domain.common.Visibility;

import java.time.LocalDateTime;

public record ExcerptSearchUnitDto(
        Long excerptId,
        String excerptContent,
        Visibility visibility,
        LocalDateTime createdTime
) {}
