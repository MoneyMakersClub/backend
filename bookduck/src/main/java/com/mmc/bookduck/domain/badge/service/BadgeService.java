package com.mmc.bookduck.domain.badge.service;

import com.mmc.bookduck.domain.badge.entity.BadgeType;
import com.mmc.bookduck.domain.badge.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;

    @Transactional(readOnly = true)
    public int countBadgesByType(BadgeType badgeType) {
        return badgeRepository.countByBadgeType(badgeType);
    }
}
