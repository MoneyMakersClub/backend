package com.mmc.bookduck.domain.user.repository;

import com.mmc.bookduck.domain.user.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
}