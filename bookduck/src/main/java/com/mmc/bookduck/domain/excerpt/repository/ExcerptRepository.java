package com.mmc.bookduck.domain.excerpt.repository;

import com.mmc.bookduck.domain.excerpt.entity.Excerpt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExcerptRepository extends JpaRepository<Excerpt, Long> {
}
