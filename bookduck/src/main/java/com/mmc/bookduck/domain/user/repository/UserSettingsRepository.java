package com.mmc.bookduck.domain.user.repository;

import com.mmc.bookduck.domain.user.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {
}