package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.archive.repository.ExcerptRepository;
import com.mmc.bookduck.domain.archive.repository.ReviewRepository;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.user.dto.common.MonthlyBookCountUnitDto;
import com.mmc.bookduck.domain.user.dto.common.MostReadGenreUnitDto;
import com.mmc.bookduck.domain.user.dto.response.UserKeywordResponseDto;
import com.mmc.bookduck.domain.user.dto.response.UserStatisticsResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
import com.mmc.bookduck.global.komoran.KomoranService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadingReportService {
    private final UserBookRepository userBookRepository;
    private final ExcerptRepository excerptRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final KomoranService komoranService;
    public UserStatisticsResponseDto getUserStatistics(Long userId) {
        // UserBook이 0권이라면 조회 불가능
        User user = userService.getActiveUserByUserId(userId);
        long userBookCount = userBookRepository.countByUser(user);
        if (userBookCount == 0) {
            throw new CustomException(ErrorCode.READINGREPORT_NOT_VIEWABLE);
        }

        // 1. 가장 많이 읽은 카테고리, Top3 카테고리
        List<Object[]> topCategoryResults = userBookRepository.findTopCategoriesByUser(user, Pageable.ofSize(3));
        List<MostReadGenreUnitDto> mostReadGenres = topCategoryResults.stream()
                .map(result -> new MostReadGenreUnitDto((String) result[0], (Long) result[1]))
                .toList();
        String duckTitle = mostReadGenres.isEmpty() ? null : mostReadGenres.get(0).genreName();

        // 2. 발췌 수, 감상평 수, 완독한 책 수
        long excerptCount = excerptRepository.countByUser(user);
        long reviewCount = reviewRepository.countByUser(user);
        long finishedBookCount = userBookRepository.countByUserAndReadStatus(user, ReadStatus.FINISHED);

        // 3. 올해 현재 분기(상반기/하반기) 월별 독서 수
        int currentMonth = java.time.LocalDate.now().getMonthValue();
        boolean isFirstHalfOfYear = (currentMonth <= 6);
        // 해당 기간의 UserBook 조회 및 월별 책 권수 카운트
        List<UserBook> userBooksForCurrentYearHalf = userBookRepository.findAllByUserAndCreatedInHalf(user, isFirstHalfOfYear);
        Map<Integer, Long> monthlyCounts = new HashMap<>();
        for (UserBook userBook : userBooksForCurrentYearHalf) {
            int month = userBook.getCreatedTime().getMonthValue();
            monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0L) + 1);
        }
        List<MonthlyBookCountUnitDto> monthlyBookCounts = monthlyCounts.entrySet().stream()
                .map(entry -> new MonthlyBookCountUnitDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(MonthlyBookCountUnitDto::month))
                .collect(Collectors.toList());

        // 4. 가장 많이 읽은 작가, 책표지들
        // 가장 많이 읽은 작가
        List<Object[]> topAuthorsResults = userBookRepository.findMostReadAuthorByUser(user);
        String mostReadAuthor = topAuthorsResults.isEmpty() ? null : (String) topAuthorsResults.get(0)[0];
        // 해당 작가의 책표지들
        List<UserBook> mostReadAuthorBooks = userBookRepository.findTop3ByBookInfo_AuthorOrderByCreatedTimeDesc(mostReadAuthor);
        List<String> imgPaths = mostReadAuthorBooks.stream()
                .map(userBook -> userBook.getBookInfo().getImgPath())
                .toList();

        return new UserStatisticsResponseDto(
                user.getNickname(),
                duckTitle,
                excerptCount + reviewCount,
                excerptCount,
                reviewCount,
                finishedBookCount,
                isFirstHalfOfYear,
                monthlyBookCounts,
                mostReadGenres,
                mostReadAuthor,
                imgPaths
        );
    }

    public List<String> analyseUserKeyword(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        List<Review> reviews = reviewRepository.findTop30ByUserOrderByCreatedTimeDesc(user);
        List<Excerpt> excerpts = excerptRepository.findTop30ByUserOrderByCreatedTimeDesc(user);

        int totalLength = 0;
        Map<String, Long> frequencyMap = new HashMap<>();

        // 감상평에서 명사와 형용사를 추출하고 글자수 누적
        for (Review review : reviews) {
            String reviewContent = review.getReviewContent();
            totalLength += reviewContent.length();
            komoranService.extractNounsAndAdjectives(reviewContent).forEach(token -> {
                frequencyMap.merge(token, 1L, Long::sum);
            });
        }

        // 발췌에서 명사와 형용사를 추출하고 글자수 누적
        for (Excerpt excerpt : excerpts) {
            String excerptContent = excerpt.getExcerptContent();
            totalLength += excerptContent.length();
            komoranService.extractNounsAndAdjectives(excerptContent).forEach(token -> {
                frequencyMap.merge(token, 1L, Long::sum);
            });
        }

        // 글자수 500자 이상 체크
        if (totalLength < 500) {
            throw new CustomException(ErrorCode.KEYWORD_NOT_VIEWABLE);
        }

        // 키워드 6개 이상일 때만 조회 가능
        List<String> topKeywords = frequencyMap.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(6)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        if (topKeywords.size() < 6) {
            throw new CustomException(ErrorCode.KEYWORD_NOT_VIEWABLE);
        }
        return topKeywords;
    }

    public UserKeywordResponseDto getUserKeywordWithLimit(Long userId, int limit) {
        List<String> allKeywords = analyseUserKeyword(userId);
        return UserKeywordResponseDto.from(allKeywords, limit);
    }

}
