package com.mmc.bookduck.domain.badge.repository;

import com.mmc.bookduck.domain.badge.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
}
