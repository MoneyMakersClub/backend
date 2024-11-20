package com.mmc.bookduck.domain.badge.service;

import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.badge.dto.common.UserBadgeUnitDto;
import com.mmc.bookduck.domain.badge.dto.response.UserBadgeListResponseDto;
import com.mmc.bookduck.domain.badge.entity.Badge;
import com.mmc.bookduck.domain.badge.entity.BadgeType;
import com.mmc.bookduck.domain.badge.entity.UserBadge;
import com.mmc.bookduck.domain.badge.repository.UserBadgeRepository;
import com.mmc.bookduck.domain.book.service.UserBookService;
import com.mmc.bookduck.domain.oneline.repository.OneLineRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserGrowthService;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBadgeService {
    private final UserBadgeRepository userBadgeRepository;
    private final OneLineRepository oneLineRepository;
    private final ReviewRepository reviewRepository;
    private final ExcerptRepository excerptRepository;
    private final UserService userService;
    private final BadgeService badgeService;
    private final UserBookService userBookService;
    private final UserGrowthService userGrowthService;

    @Transactional(readOnly = true)
    public UserBadgeListResponseDto getCurrentUserBadges() {
        User user = userService.getCurrentUser();
        List<UserBadge> uniqueUserBadges = deleteDuplicateUserBadges(userBadgeRepository.findAllByUser(user));

        // 전체 뱃지
        List<Badge> allBadges = badgeService.getAllBadges();

        // 사용자가 가진 뱃지를 badgeId로 맵핑
        Map<Long, UserBadge> userBadgeMap = uniqueUserBadges.stream()
                .collect(Collectors.toMap(badge -> badge.getBadge().getBadgeId(), badge -> badge));

        // 모든 뱃지와 isOwned를 결합한 리스트 생성
        List<UserBadgeUnitDto> allBadgesWithOwnership = allBadges.stream()
                .map(badge -> UserBadgeUnitDto.from(badge, userBadgeMap.get(badge.getBadgeId()), getBadgeUnlockValue(badge)))
                .collect(Collectors.toList());

        // BadgeType별로 뱃지 리스트 나누기
        List<UserBadgeUnitDto> readBadgeList = filterByBadgeType(allBadgesWithOwnership, BadgeType.READ);
        List<UserBadgeUnitDto> archiveBadgeList = filterByBadgeType(allBadgesWithOwnership, BadgeType.ARCHIVE);
        List<UserBadgeUnitDto> oneLineBadgeList = filterByBadgeType(allBadgesWithOwnership, BadgeType.ONELINE);
        List<UserBadgeUnitDto> levelBadgeList = filterByBadgeType(allBadgesWithOwnership, BadgeType.LEVEL);

        // 각 분야별 사용자 상태 가져오기
        long currentReadCount = userBookService.countFinishedUserBooksByUser(user);
        long currentArchiveCount = reviewRepository.countByUser(user) + excerptRepository.countByUser(user);
        long currentOneLineCount = oneLineRepository.countAllByUser(user);
        long currentLevel = userGrowthService.getUserGrowthByUserId(user.getUserId()).getLevel();

        return new UserBadgeListResponseDto(
                currentReadCount,
                currentArchiveCount,
                currentOneLineCount,
                currentLevel,
                readBadgeList,
                archiveBadgeList,
                oneLineBadgeList,
                levelBadgeList
        );
    }

    private int getBadgeUnlockValue(Badge badge) {
        try {
            return Integer.parseInt(badge.getUnlockCondition());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private List<UserBadgeUnitDto> filterByBadgeType(List<UserBadgeUnitDto> allBadgesWithOwnership, BadgeType badgeType) {
        return allBadgesWithOwnership.stream()
                .filter(dto -> dto.badgeType().equals(badgeType))
                .collect(Collectors.toList());
    }

    public List<UserBadge> deleteDuplicateUserBadges(List<UserBadge> userBadges) {
        // 중복 제거: 동일한 badgeId를 가진 UserBadge가 여러 개 있으면 하나만 남기기
        Map<Long, UserBadge> badgeIdToUniqueUserBadgeMap = userBadges.stream()
                .collect(Collectors.toMap(
                        userBadge -> userBadge.getBadge().getBadgeId(), // 중복 제거 기준: badgeId
                        userBadge -> userBadge,
                        (existing, duplicate) -> existing // 중복 시 기존 항목 유지
                ));

        // distinctUserBadges에 포함되지 않는 중복 UserBadge 식별
        List<UserBadge> duplicateUserBadges = userBadges.stream()
                .filter(userBadge -> !badgeIdToUniqueUserBadgeMap.containsValue(userBadge))
                .collect(Collectors.toList());

        // 중복된 UserBadge 삭제
        if (!duplicateUserBadges.isEmpty()) {
            userBadgeRepository.deleteAll(duplicateUserBadges);
        }

        return new ArrayList<>(badgeIdToUniqueUserBadgeMap.values());
    }
}
