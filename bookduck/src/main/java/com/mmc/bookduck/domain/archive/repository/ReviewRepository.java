package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUserAndReviewTitleStartingWith(User user, String reviewTitle);

    long countByUser(User user);

    long countByUserAndCreatedTimeBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user AND YEAR(r.createdTime) = :currentYear")
    long countByUserAndCreatedTimeThisYear(@Param("user") User user, @Param("currentYear") int currentYear);

    List<Review> findReviewByUserBookOrderByCreatedTimeDesc(UserBook userBook);


    @Query("SELECT r FROM Review r WHERE r.userBook = :userBook AND (r.visibility = 'PUBLIC') ORDER BY r.createdTime DESC")
    List<Review> findReviewsByUserBookWithPublic(@Param("userBook") UserBook userBook);

    List<Review> findTop30ByUserOrderByCreatedTimeDesc(User user);


    List<Review> findAllByUserAndCreatedTimeAfter(User user, LocalDateTime createdTime);

    @Query("SELECT e FROM Review e WHERE e.user.userId = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);
}
