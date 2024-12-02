package com.mmc.bookduck.domain.badge.service;

import com.mmc.bookduck.domain.badge.entity.Badge;
import com.mmc.bookduck.domain.badge.entity.BadgeType;
import com.mmc.bookduck.domain.badge.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;

    @Transactional(readOnly = true)
    public int countBadgesByType(BadgeType badgeType) {
        return badgeRepository.countByBadgeType(badgeType);
    }

    @Transactional(readOnly = true)
    public List<Badge> getAllBadgesByType(BadgeType badgeType) {
        return badgeRepository.findAllByBadgeType(badgeType);
    }

    @Transactional(readOnly = true)
    public List<Badge> getAllBadges() {
        return badgeRepository.findAll();
    }

    // 뱃지 잠금해제 조건을 int로 가져옴
    public int getBadgeUnlockValue(Badge badge) {
        try {
            return Integer.parseInt(badge.getUnlockCondition());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
