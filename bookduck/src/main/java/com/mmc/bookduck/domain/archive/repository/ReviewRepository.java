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

    long countByUser(User user);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user AND YEAR(r.createdTime) = :currentYear")
    long countByUserAndCreatedTimeThisYear(@Param("user") User user, @Param("currentYear") int currentYear);

    List<Review> findReviewByUserBookOrderByCreatedTimeDesc(UserBook userBook);

    List<Review> findAllByUserAndCreatedTimeAfter(User user, LocalDateTime createdTime);

    @Query("SELECT e FROM Review e WHERE e.user.id = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);
}
