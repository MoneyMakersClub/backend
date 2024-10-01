package com.mmc.bookduck.domain.badge.repository;

import com.mmc.bookduck.domain.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
