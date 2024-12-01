package com.mmc.bookduck.domain.badge.repository;

import com.mmc.bookduck.domain.badge.entity.UserBadge;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findAllByUser(User user);
    void deleteAllByUser(User user);
}
