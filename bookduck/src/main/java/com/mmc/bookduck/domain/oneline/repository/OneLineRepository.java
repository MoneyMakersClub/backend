package com.mmc.bookduck.domain.oneline.repository;

import com.mmc.bookduck.domain.book.entity.BookInfo;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.user.entity.User;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OneLineRepository extends JpaRepository<OneLine, Long> {
    Optional<OneLine> findByUserBook(UserBook userBook);

    int countAllByUser(User user);

    // 좋아요 수 내림차순 정렬
    @Query("SELECT o FROM OneLine o WHERE o.userBook.bookInfo = :bookInfo ORDER BY SIZE(o.oneLineLikes) DESC")
    Page<OneLine> findByBookInfoOrderByLikesDesc(@Param("bookInfo") BookInfo bookInfo, Pageable pageable);

    // 생성일 내림차순 정렬
    @Query("SELECT o FROM OneLine o WHERE o.userBook.bookInfo = :bookInfo ORDER BY o.createdTime DESC")
    Page<OneLine> findByBookInfoOrderByCreatedTimeDesc(@Param("bookInfo") BookInfo bookInfo, Pageable pageable);

    // 별점 높은 순 정렬, null은 마지막
    @Query("SELECT o FROM OneLine o WHERE o.userBook.bookInfo = :bookInfo ORDER BY o.userBook.rating DESC")
    Page<OneLine> findByBookInfoOrderByRatingDesc(@Param("bookInfo") BookInfo bookInfo, Pageable pageable);

    // 별점 낮은 순 정렬, null은 마지막
    @Query("SELECT o FROM OneLine o WHERE o.userBook.bookInfo = :bookInfo ORDER BY o.userBook.rating ASC")
    Page<OneLine> findByBookInfoOrderByRatingAsc(@Param("bookInfo") BookInfo bookInfo, Pageable pageable);
}
