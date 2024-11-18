package com.mmc.bookduck.domain.alarm.dto.common;

import com.mmc.bookduck.domain.alarm.entity.Announcement;

import java.time.LocalDateTime;

public record AnnouncementUnitDto(
        Long announcementId,
        LocalDateTime createdTime,
        String title,
        String content
) {
    public AnnouncementUnitDto(Announcement announcement) {
        this(
                announcement.getAnnouncementId(),
                announcement.getCreatedTime(),
                announcement.getTitle(),
                announcement.getContent()
        );
    }
}