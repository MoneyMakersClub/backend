package com.mmc.bookduck.domain.archive.repository;


import com.mmc.bookduck.domain.archive.entity.Archive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    Optional<Archive> findByExcerpt_ExcerptId(Long excerptId);

    Optional<Archive> findByReview_ReviewId(Long reviewId);

}
