package com.mmc.bookduck.domain.user.repository;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.entity.UserGrowth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGrowthRepository extends JpaRepository<UserGrowth, Long> {
    Optional<UserGrowth> findByUser(User user);
}