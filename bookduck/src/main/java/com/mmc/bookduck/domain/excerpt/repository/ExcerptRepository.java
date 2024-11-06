package com.mmc.bookduck.domain.excerpt.repository;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.archive.entity.Excerpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExcerptRepository extends JpaRepository<Excerpt, Long> {
    long countByUser(User user);

    @Query("SELECT COUNT(e) FROM Excerpt e WHERE e.user = :user AND YEAR(e.createdTime) = :currentYear")
    long countByUserAndCreatedTimeThisYear(@Param("user") User user, @Param("currentYear") int currentYear);
}