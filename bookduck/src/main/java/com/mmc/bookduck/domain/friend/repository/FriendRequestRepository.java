package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
