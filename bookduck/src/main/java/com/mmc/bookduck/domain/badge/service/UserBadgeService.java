package com.mmc.bookduck.domain.badge.service;

import com.mmc.bookduck.domain.badge.dto.common.UserBadgeUnitDto;
import com.mmc.bookduck.domain.badge.dto.response.UserBadgeResponseDto;
import com.mmc.bookduck.domain.badge.entity.BadgeType;
import com.mmc.bookduck.domain.badge.entity.UserBadge;
import com.mmc.bookduck.domain.badge.repository.UserBadgeRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserBadgeService {
    private final UserBadgeRepository userBadgeRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public UserBadgeResponseDto getUserBadges(Long userId) {
        User user = userService.getUserByUserId(userId);
        List<UserBadge> userBadges = userBadgeRepository.findAllByUser(user);
        List<UserBadge> uniqueUserBadges = deleteDuplicateUserBadges(userBadges);

        // BadgeType별로 그룹화
        Map<BadgeType, List<UserBadge>> badgesByType = uniqueUserBadges.stream()
                .collect(Collectors.groupingBy(userBadge -> userBadge.getBadge().getBadgeType()));

        // BadgeType에 따라 리스트 생성
        List<UserBadgeUnitDto> readBadgeList = convertToDtoList(badgesByType.getOrDefault(BadgeType.READ, List.of()));
        List<UserBadgeUnitDto> archiveBadgeList = convertToDtoList(badgesByType.getOrDefault(BadgeType.ARCHIVE, List.of()));
        List<UserBadgeUnitDto> orlBadgeList = convertToDtoList(badgesByType.getOrDefault(BadgeType.ORL, List.of()));
        List<UserBadgeUnitDto> levelBadgeList = convertToDtoList(badgesByType.getOrDefault(BadgeType.LEVEL, List.of()));

        return new UserBadgeResponseDto(readBadgeList, archiveBadgeList, orlBadgeList, levelBadgeList);
    }

    private List<UserBadgeUnitDto> convertToDtoList(List<UserBadge> userBadges) {
        return userBadges.stream()
                .map(UserBadgeUnitDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
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
