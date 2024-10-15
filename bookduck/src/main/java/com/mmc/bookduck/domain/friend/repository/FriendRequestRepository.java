package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderUserIdAndReceiverUserIdAndFriendRequestStatus(Long senderId, Long receiverId, FriendRequestStatus status); // 특정한 한 케이스므로 Optional
    List<FriendRequest> findByReceiverUserIdAndFriendRequestStatus(Long receiverId, FriendRequestStatus status);
    List<FriendRequest> findBySenderUserIdAndFriendRequestStatus(Long receiverId, FriendRequestStatus status);
}
