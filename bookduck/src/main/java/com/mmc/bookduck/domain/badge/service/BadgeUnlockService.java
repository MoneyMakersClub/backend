package com.mmc.bookduck.domain.badge.service;

import com.mmc.bookduck.domain.alarm.service.AlarmByTypeService;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.badge.entity.Badge;
import com.mmc.bookduck.domain.badge.dto.common.UserActivityDto;
import com.mmc.bookduck.domain.badge.entity.UserBadge;
import com.mmc.bookduck.domain.badge.repository.UserBadgeRepository;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.oneline.repository.OneLineRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.repository.UserGrowthRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeUnlockService {
    private final UserBadgeRepository userBadgeRepository;
    private final UserBookRepository userBookRepository;
    private final ReviewRepository reviewRepository;
    private final ExcerptRepository excerptRepository;
    private final OneLineRepository oneLineRepository;
    private final BadgeService badgeService;
    private final UserGrowthRepository userGrowthRepository;
    private final AlarmByTypeService alarmByTypeService;

    // 유저 활동 가져오기
    public UserActivityDto getUserActivity(User user) {
        // 각 활동 데이터 조회
        long readCount = userBookRepository.countByUserAndReadStatus(user, ReadStatus.FINISHED);
        long archiveCount = reviewRepository.countByUser(user) + excerptRepository.countByUser(user);
        long oneLineCount = oneLineRepository.countAllByUser(user);
        long level = userGrowthRepository.findByUser(user).orElseThrow(()-> new CustomException(ErrorCode.USERGROWTH_NOT_FOUND)).getLevel();

        // 결과 반환
        return new UserActivityDto(readCount, archiveCount, oneLineCount, level);
    }

    // 뱃지 획득 트리거
    public void checkAndUnlockBadges(User user) {
        // 사용자 활동 데이터 가져오기
        UserActivityDto userActivityDto = getUserActivity(user);

        // 현재 사용자가 이미 보유한 뱃지
        List<Long> ownedBadgeIds = userBadgeRepository.findAllByUser(user).stream()
                .map(userBadge -> userBadge.getBadge().getBadgeId())
                .toList();

        // 전체 뱃지 가져오기
        List<Badge> allBadges = badgeService.getAllBadges();

        for (Badge badge : allBadges) {
            if (ownedBadgeIds.contains(badge.getBadgeId())) {
                continue; // 이미 보유한 뱃지는 스킵
            }

            // 뱃지 조건 확인
            boolean isConditionMet = isBadgeConditionMet(badge, userActivityDto);

            if (isConditionMet) {
                // UserBadge 생성 및 저장
                UserBadge userBadge = UserBadge.builder()
                        .user(user)
                        .badge(badge)
                        .build();
                userBadgeRepository.save(userBadge);

                // 뱃지 획득 알림 생성
                alarmByTypeService.createBadgeUnlockedAlarm(user, userBadge);
            }
        }
    }

    // 뱃지 획득조건 충족여부 확인
    private boolean isBadgeConditionMet(Badge badge, UserActivityDto activity) {
        int unlockValue = badgeService.getBadgeUnlockValue(badge);

        return switch (badge.getBadgeType()) {
            case READ -> activity.readCount() >= unlockValue;
            case ARCHIVE -> activity.archiveCount() >= unlockValue;
            case ONELINE -> activity.oneLineCount() >= unlockValue;
            case LEVEL -> activity.level() >= unlockValue;
        };
    }
}