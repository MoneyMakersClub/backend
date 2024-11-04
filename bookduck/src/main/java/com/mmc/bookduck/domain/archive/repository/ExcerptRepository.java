package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcerptRepository extends JpaRepository<Excerpt, Long> {
}
