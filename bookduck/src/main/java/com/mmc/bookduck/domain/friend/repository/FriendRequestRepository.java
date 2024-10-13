package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findBySenderIdAndReceiverIdAndFriendRequestStatus(Long senderId, Long receiverId, FriendRequestStatus status);
    List<FriendRequest> findByReceiverIdAndFriendRequestStatus(Long receiverId, FriendRequestStatus status);
}
