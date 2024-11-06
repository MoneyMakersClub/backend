package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.excerpt.repository.ExcerptRepository;
import com.mmc.bookduck.domain.review.repository.ReviewRepository;
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
    public UserGrowthInfoResponseDto getUserGrowthInfo(Long userId) {
        UserGrowth userGrowth = getUserGrowthByUserId(userId);

        // 현재 레벨과 누적 경험치
        int level = userGrowth.getLevel();
        long cumulativeExp = userGrowth.getCumulativeExp();

        // 현재 레벨의 기준 경험치와 다음 레벨 기준 경험치 계산
        long currentLevelExpThreshold = userGrowth.calculateExpThresholdForLevel(level);
        long nextLevelExpThreshold = userGrowth.calculateExpThresholdForLevel(level + 1);

        // 이번 레벨에서 쌓은 경험치와 다음 레벨로 넘어가기 위한 경험치 계산
        long expInCurrentLevel = cumulativeExp - currentLevelExpThreshold;
        long expToNextLevel = nextLevelExpThreshold - currentLevelExpThreshold;

        // 응답 DTO 생성 및 반환
        return new UserGrowthInfoResponseDto(level, expInCurrentLevel, expToNextLevel);
    }

    // 경험치 증가 메소드
    // TODO: 수치 변경 필요
    public void gainExcerptExp(UserBook userBook) {
        gainExpForUser(userBook, 20, "Excerpt");
    }

    public void gainReviewExp(UserBook userBook) {
        gainExpForUser(userBook, 30, "Review");
    }

    public void gainOlrExp(UserBook userBook) {
        gainExpForUser(userBook, 50, "Olr");
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
            case "Excerpt" -> userBook.markExcerptExpGiven();
            case "Review" -> userBook.markReviewExpGiven();
            case "Olr" -> userBook.markOlrExpGiven();
            default -> throw new IllegalArgumentException("Unsupported experience type: " + experienceType);
        }
    }
}
