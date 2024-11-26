package com.mmc.bookduck.domain.alarm.service;

import com.mmc.bookduck.domain.alarm.dto.common.AnnouncementUnitDto;
import com.mmc.bookduck.domain.alarm.entity.Announcement;
import com.mmc.bookduck.domain.alarm.repository.AnnouncementRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.common.PaginatedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;
    private final UserService userService;
    private final EmitterService emitterService;

    public PaginatedResponseDto<AnnouncementUnitDto> getRecentAnnouncements(Pageable pageable) {
        // user가 공지 읽음으로 표시
        User user = userService.getCurrentUser();
        user.setIsAnnouncementChecked(true);

        Page<Announcement> announcementPage = announcementRepository.findByOrderByCreatedTimeDesc(pageable);
        Page<AnnouncementUnitDto> annoucementUnitDtos = announcementPage.map(AnnouncementUnitDto::new);
        emitterService.sendToClientIfNewAlarmExists(user);
        return PaginatedResponseDto.from(annoucementUnitDtos);
    }
}
