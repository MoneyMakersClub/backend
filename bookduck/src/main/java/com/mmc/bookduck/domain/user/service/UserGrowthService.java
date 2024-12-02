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
        long bookCount = (reviewCount + excerptCount);
        boolean isOfficial = targetUser.isOfficial();
        User currentUser = userService.getCurrentUserOrNull();

        // 유저와의 관계 상태
        UserRelationshipStatusDto userRelationshipStatusDto = userRelationshipService.getUserRelationshipStatus(currentUser, targetUser);
        return new UserInfoResponseDto(targetUser.getNickname(), bookCount, isOfficial, userRelationshipStatusDto);
    }

    @Transactional(readOnly = true)
    public UserGrowthInfoResponseDto getUserLevelInfo(Long userId) {
        User targetUser = userService.getActiveUserByUserId(userId);
        UserGrowth userGrowth = getUserGrowthByUser(targetUser);
        long expInCurrentLevel = userGrowth.getCumulativeExp(); // 현재 레벨의 경험치

        int level = 1;
        long expThreshold = userGrowth.calculateExpThresholdForNextLevel(level); // 첫 레벨의 경계값 계산

        // 레벨을 계산하고, 각 레벨마다 해당 경계값을 재계산
        while (expInCurrentLevel >= expThreshold) {
            level++;
            expInCurrentLevel -= expThreshold;
            expThreshold = userGrowth.calculateExpThresholdForNextLevel(level);  // 다음 레벨에 맞는 경계값을 계산
        }

        return new UserGrowthInfoResponseDto(level, expInCurrentLevel, userGrowth.calculateExpThresholdForNextLevel(level));
    }

    // 경험치 증가 메소드
    public void gainExpForFinishedBook(UserBook userBook) {
        if (!userBook.isFinishedExpGiven()) {
            gainExpForUser(userBook.getUser(), 20);
            userBook.markFinishedExpGiven();
        }
    }

    public void gainExpForArchive(UserBook userBook) {
        if (!userBook.isArchiveExpGiven()) {
            gainExpForUser(userBook.getUser(), 30);
            userBook.markArchiveExpGiven();
        }
    }

    public void gainExpForOneLine(UserBook userBook) {
        if (!userBook.isOneLineExpGiven()) {
            gainExpForUser(userBook.getUser(), 50);
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