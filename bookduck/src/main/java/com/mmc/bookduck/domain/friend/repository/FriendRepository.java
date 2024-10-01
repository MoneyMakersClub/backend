package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
