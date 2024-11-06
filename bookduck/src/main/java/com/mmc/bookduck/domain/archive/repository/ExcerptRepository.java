package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ExcerptRepository extends JpaRepository<Excerpt, Long> {
    Optional<Excerpt> findByUserUserIdAndCreatedTime(Long userId, LocalDateTime createdTime);
}
