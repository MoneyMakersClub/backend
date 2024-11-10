package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.dto.response.UserGrowthInfoResponseDto;
import com.mmc.bookduck.domain.user.dto.response.UserInfoResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserGrowth;
import com.mmc.bookduck.domain.user.repository.UserGrowthRepository;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@Transactional
@RequiredArgsConstructor
public class UserGrowthService {
    private final UserGrowthRepository userGrowthRepository;
    private final ExcerptRepository excerptRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public UserGrowth getUserGrowthByUserId(Long userId)
    {
        User user = userService.getUserByUserId(userId);
        return userGrowthRepository.findByUser(user)
                .orElseThrow(()-> new CustomException(ErrorCode.USERGROWTH_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long userId) {
        User user = userService.getUserByUserId(userId);
        int currentYear = Year.now().getValue();
        long reviewCount = reviewRepository.countByUserAndCreatedTimeThisYear(user, currentYear);
        long excerptCount = excerptRepository.countByUserAndCreatedTimeThisYear(user, currentYear);
        long bookCount = (reviewCount + excerptCount);
        return new UserInfoResponseDto(user.getNickname(), bookCount);
    }

    @Transactional(readOnly = true)
    public UserGrowthInfoResponseDto getUserLevelInfo(Long userId) {
        UserGrowth userGrowth = getUserGrowthByUserId(userId);
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
    public void gainFinishedExp(UserBook userBook) {
        gainExpForUser(userBook, 20, "Finished");
    }

    public void gainArchiveExp(UserBook userBook) {
        gainExpForUser(userBook, 30, "Archive");
    }

    public void gainOneLineExp(UserBook userBook) {
        gainExpForUser(userBook, 50, "OneLine");
    }

    private void gainExpForUser(UserBook userBook, int expToAdd, String experienceType) {
        Long userId = userBook.getUser().getUserId();
        UserGrowth userGrowth = getUserGrowthByUserId(userId);
        userGrowth.gainExp(expToAdd);
        // TODO: 레벨 상승 알림 추가
        userGrowthRepository.save(userGrowth);
        markExpGiven(userBook, experienceType);
    }

    private void markExpGiven(UserBook userBook, String experienceType) {
        switch (experienceType) {
            case "Finished" -> userBook.markFinishedExpGiven();
            case "Archive" -> userBook.markArchiveExpGiven();
            case "OneLine" -> userBook.markOneLineExpGiven();
            default -> throw new IllegalArgumentException("Unsupported experience type: " + experienceType);
        }
    }
}