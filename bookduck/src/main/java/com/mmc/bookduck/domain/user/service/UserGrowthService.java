package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.alarm.service.AlarmByTypeService;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.badge.service.BadgeUnlockService;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.friend.service.FriendService;
import com.mmc.bookduck.domain.user.dto.UserRelationshipStatusDto;
import com.mmc.bookduck.domain.user.dto.response.UserGrowthInfoResponseDto;
import com.mmc.bookduck.domain.user.dto.response.UserInfoResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserGrowth;
import com.mmc.bookduck.domain.user.entity.UserRelationshipStatus;
import com.mmc.bookduck.domain.user.repository.UserGrowthRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserGrowthService {
    private final UserGrowthRepository userGrowthRepository;
    private final ExcerptRepository excerptRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final UserRelationshipService userRelationshipService;
    private final AlarmByTypeService alarmByTypeService;
    private final BadgeUnlockService badgeUnlockService;

    @Transactional(readOnly = true)
    public UserGrowth getUserGrowthByUser(User user) {
        return userGrowthRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.USERGROWTH_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long userId) {
        User targetUser = userService.getActiveUserByUserId(userId);
        long reviewCount = reviewRepository.countByUser(targetUser);
        long excerptCount = excerptRepository.countByUser(targetUser);
        long bookRecordCount = (reviewCount + excerptCount);
        boolean isOfficial = targetUser.isOfficial();
        User currentUser = userService.getCurrentUserOrNull();

        // 유저와의 관계 상태
        UserRelationshipStatusDto userRelationshipStatusDto = userRelationshipService.getUserRelationshipStatus(currentUser, targetUser);
        return new UserInfoResponseDto(targetUser.getNickname(), bookRecordCount, isOfficial, userRelationshipStatusDto);
    }

    @Transactional(readOnly = true)
    public UserGrowthInfoResponseDto getUserLevelInfo(Long userId) {
        User targetUser = userService.getActiveUserByUserId(userId);
        UserGrowth userGrowth = getUserGrowthByUser(targetUser);
        long cumulativeExp = userGrowth.getCumulativeExp(); // 누적 경험치

        int level = 1;
        long nextLevelExp = userGrowth.calculateExpThresholdForNextLevel(level); // 첫 번째 레벨의 누적 경험치 요구량

        // 누적 경험치 기준으로 레벨 계산
        while (cumulativeExp >= nextLevelExp) {
            level++;
            nextLevelExp = userGrowth.calculateExpThresholdForNextLevel(level); // 다음 레벨의 누적 경험치 요구량
        }

        // 현재 레벨에서의 진행 경험치와 다음 레벨까지 필요한 경험치 계산
        long expForCurrentLevel = cumulativeExp - userGrowth.calculateExpThresholdForNextLevel(level - 1);
        long expThresholdForNextLevel = nextLevelExp - userGrowth.calculateExpThresholdForNextLevel(level - 1);
        return new UserGrowthInfoResponseDto(level, expForCurrentLevel, expThresholdForNextLevel);
    }

    // 경험치 증가 메소드
    public void gainExpForFinishedBook(UserBook userBook) {
        if (!userBook.isFinishedExpGiven()) {
            gainExpForUser(userBook.getUser(), 5);
            userBook.markFinishedExpGiven();
        }
    }

    public void gainExpForArchive(UserBook userBook) {
        if (!userBook.isArchiveExpGiven()) {
            gainExpForUser(userBook.getUser(), 15);
            userBook.markArchiveExpGiven();
        }
    }

    public void gainExpForOneLine(UserBook userBook) {
        if (!userBook.isOneLineExpGiven()) {
            gainExpForUser(userBook.getUser(), 25);
            userBook.markOneLineExpGiven();
        }
    }

    private void gainExpForUser(User user, int expToAdd) {
        UserGrowth userGrowth = getUserGrowthByUser(user);

        boolean isLevelUp = userGrowth.gainExp(expToAdd);
        if (isLevelUp) {
            alarmByTypeService.createLevelUpAlarm(user, userGrowth.getLevel());
            // LEVELUP 뱃지 unlock 확인
            badgeUnlockService.checkAndUnlockBadges(user);
        }
        userGrowthRepository.save(userGrowth);
    }
}