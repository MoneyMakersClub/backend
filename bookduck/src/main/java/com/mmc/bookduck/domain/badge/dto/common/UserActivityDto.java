package com.mmc.bookduck.domain.badge.dto.common;

public record UserActivityDto(
        long readCount,
        long archiveCount,
        long oneLineCount,
        long level
) {}
