package com.mmc.bookduck.domain.review.repository;

import com.mmc.bookduck.domain.archive.entity.Review;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    long countByUser(User user);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.user = :user AND YEAR(r.createdTime) = :currentYear")
    long countByUserAndCreatedTimeThisYear(@Param("user") User user, @Param("currentYear") int currentYear);
}
