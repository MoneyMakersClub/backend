package com.mmc.bookduck.domain.user.repository;

import com.mmc.bookduck.domain.user.entity.UserHome;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHomeRepository extends JpaRepository<UserHome, Long> {
}