package com.mmc.bookduck.domain.userhome.repository;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserHomeRepository extends JpaRepository<UserHome, Long> {
    Optional<UserHome> findByUser(User user);
}