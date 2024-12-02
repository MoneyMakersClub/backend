package com.mmc.bookduck.domain.badge.entity;

public record UserActivity(
        long readCount,
        long archiveCount,
        long oneLineCount,
        long level
) {}
