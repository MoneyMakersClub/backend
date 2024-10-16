package com.mmc.bookduck.domain.user.repository;

import com.mmc.bookduck.domain.user.entity.UserGrowth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGrowthRepository extends JpaRepository<UserGrowth, Long> {
}