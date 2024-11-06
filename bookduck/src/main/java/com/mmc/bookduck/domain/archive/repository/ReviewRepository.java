package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserUserIdAndCreatedTime(Long userId, LocalDateTime createdTime);

    long countByUser(User user);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user AND YEAR(r.createdTime) = :currentYear")
    long countByUserAndCreatedTimeThisYear(@Param("user") User user, @Param("currentYear") int currentYear);
}
