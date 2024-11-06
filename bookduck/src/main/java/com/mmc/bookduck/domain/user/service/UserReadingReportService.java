package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.excerpt.repository.ExcerptRepository;
import com.mmc.bookduck.domain.review.repository.ReviewRepository;
import com.mmc.bookduck.domain.user.dto.common.MonthlyBookCountUnitDto;
import com.mmc.bookduck.domain.user.dto.common.MostReadGenreUnitDto;
import com.mmc.bookduck.domain.user.dto.response.UserStatisticsResponseDto;
import com.mmc.bookduck.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserReadingReportService {
    private final UserBookRepository userBookRepository;
    private final ExcerptRepository excerptRepository;
    private final ReviewRepository reviewRepository;
    private final UserService userService;

    public UserStatisticsResponseDto getUserStatistics(Long userId) {
        User user = userService.getUserByUserId(userId);

        // 1. 가장 많이 읽은 카테고리, Top3 카테고리 // TODO: 카테고리? GenreName?
        List<Object[]> topCategoryResults = userBookRepository.findTopCategoriesByUser(user, Pageable.ofSize(3));
        List<MostReadGenreUnitDto> mostReadGenres = topCategoryResults.stream()
                .map(result -> new MostReadGenreUnitDto((String) result[0], (Long) result[1]))
                .toList();
        // 널 체크
        String duckTitle = mostReadGenres.isEmpty() ? null : mostReadGenres.get(0).genreName();


        // 2. 발췌 수, 감상평 수, 완독한 책 수
        long excerptCount = excerptRepository.countByUser(user);
        long reviewCount = reviewRepository.countByUser(user);
        List<UserBook> userBooks = userBookRepository.findByUserAndReadStatus(user, ReadStatus.FINISHED);
        long finishedBookCount = userBooks.size();


        // 3. 올해 현재 분기(상반기/하반기) 월별 독서 수
        int currentMonth = java.time.LocalDate.now().getMonthValue();
        boolean isFirstHalfOfYear = (currentMonth <= 6);
        // 해당 기간의 UserBook 조회 (TODO: ReadStatus 재확인 필요)
        List<UserBook> userBooksForCurrentYearHalf = userBookRepository.findAllByUserAndCreatedInHalfWithReadStatus(user, isFirstHalfOfYear, ReadStatus.FINISHED);
        // 월별 독서 수를 저장할 Map
        Map<Integer, Long> monthlyCounts = new HashMap<>();
        // 각 책의 생성 월에 따라 카운트
        for (UserBook userBook : userBooksForCurrentYearHalf) {
            int month = userBook.getCreatedTime().getMonthValue();
            monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0L) + 1);
        }
        // 월별 책 권수
        List<MonthlyBookCountUnitDto> monthlyBookCounts = monthlyCounts.entrySet().stream()
                .map(entry -> new MonthlyBookCountUnitDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingInt(MonthlyBookCountUnitDto::month)) // 월 기준으로 정렬
                .collect(Collectors.toList());


        // 4. 가장 많이 읽은 작가, 책표지들
        // 가장 많이 읽은 작가
        List<Object[]> topAuthorsResults = userBookRepository.findMostReadAuthorByUser(user);
        String mostReadAuthor = topAuthorsResults.isEmpty() ? null : (String) topAuthorsResults.get(0)[0];
        // 해당 작가의 책표지들
        List<UserBook> mostReadAuthorBooks = userBookRepository.findAllByBookInfo_Author(mostReadAuthor);
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

//    public Object getUserKeywordAnalysis(Long userId) {
//        User user = userService.getUserByUserId(userId);
//    }
}
