package com.mmc.bookduck.domain.book.repository;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    Optional<UserBook> findUserBookByUserBookId(Long userBookId);

    List<UserBook> findByUserAndReadStatus(User user, ReadStatus readStatus);

    long countByUser(User user);

    long countByUserAndReadStatus(User user, ReadStatus readStatus);

    long countByUserAndReadStatusAndCreatedTimeBetween(User user, ReadStatus readStatus, LocalDateTime startDate, LocalDateTime endDate);

    // userbook 테이블과 bookinfo 테이블 조인해서 userbook의 user에 해당하는 bookinfo 정보 검색
    @Query("SELECT ub FROM UserBook ub JOIN ub.bookInfo b WHERE ub.user = :userId AND (" +
            "b.title LIKE %:keyword% OR b.author LIKE %:keyword% OR b.description LIKE %:keyword%)")
    List<UserBook> searchByUserIdAndKeyword(@Param("userId") User user, @Param("keyword") String keyword);

    Optional<UserBook> findByUserAndBookInfo(User user, BookInfo bookInfo);

    //최신순
    List<UserBook> findAllByUserOrderByCreatedTimeDesc(User user);

    //제목순
    @Query("SELECT ub FROM UserBook ub JOIN ub.bookInfo b WHERE ub.user = :user ORDER BY b.title ASC")
    List<UserBook> findAllByUserOrderByTitle(User user);

    //별점 높은 순
    List<UserBook> findByUserOrderByRatingDesc(User user);

    // 별점 낮은 순
    List<UserBook> findByUserOrderByRatingAsc(User user);

    List<UserBook> findAllByBookInfo(BookInfo bookInfo);

    List<UserBook> findAllByUser(User user);

    // 유저가 가장 많이 읽은 장르들
    @Query("SELECT g.genreName, COUNT(ub) FROM UserBook ub " +
            "JOIN ub.bookInfo bi " +
            "JOIN bi.genre g " +
            "WHERE ub.user = :user " +
            "GROUP BY g.genreName " +
            "ORDER BY COUNT(ub) DESC")
    List<Object[]> findTopGenresByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT g.genreName " +
            "FROM UserBook ub " +
            "JOIN ub.bookInfo b " +
            "JOIN b.genre g " +
            "WHERE ub.user = :user " +
            "AND ub.createdTime BETWEEN :startDate AND :endDate " +
            "GROUP BY g.genreName " +
            "ORDER BY COUNT(ub) DESC")
    List<GenreName> findTopGenreByUserAndCreatedTimeBetween(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 유저가 가장 많이 읽은 작가들
    @Query(value = "SELECT b.author, COUNT(ub) AS bookCount FROM UserBook ub " +
            "JOIN ub.bookInfo b " +
            "WHERE ub.user = :user " +
            "GROUP BY b.author " +
            "ORDER BY bookCount DESC")
    List<Object[]> findMostReadAuthorByUser(@Param("user") User user);

    @Query("SELECT ub.bookInfo.author " +
            "FROM UserBook ub " +
            "WHERE ub.user = :user " +
            "AND YEAR(ub.createdTime) = YEAR(CURRENT_DATE) " +  // 올해 데이터만
            "AND ub.createdTime BETWEEN :startDate AND :endDate " +  // 주어진 기간
            "GROUP BY ub.bookInfo.author " +
            "ORDER BY COUNT(ub) DESC")
    List<String> findTopAuthorByUserAndCreatedTimeBetween(@Param("user") User user,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);
    // BookInfo의 작가명으로 UserBook top3찾기
    List<UserBook> findTop3ByBookInfo_AuthorOrderByCreatedTimeDesc(String author);

    // 유저의 UserBook들 올해 상반기/하반기별로 조회
    @Query("SELECT ub FROM UserBook ub " +
            "WHERE ub.user = :user " +
            "AND YEAR(ub.createdTime) = :year " +
            "AND ((:isFirstHalf = true AND MONTH(ub.createdTime) BETWEEN 1 AND 6) " +
            "OR (:isFirstHalf = false AND MONTH(ub.createdTime) BETWEEN 7 AND 12))")
    List<UserBook> findAllByUserAndCreatedInYearAndHalf(@Param("user") User user, @Param("year") int year, @Param("isFirstHalf") boolean isFirstHalf);

    List<UserBook> findAllByBookInfoOrderByRatingDesc(BookInfo bookInfo);

    List<UserBook> findAllByCreatedTimeAfter(LocalDateTime createdTime);

    List<UserBook> findAllByUserAndIsArchiveExpGivenTrue(User user);
}