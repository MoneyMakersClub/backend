package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.FriendRequest;
import com.mmc.bookduck.domain.friend.entity.FriendRequestStatus;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    @Query("SELECT fr FROM FriendRequest fr WHERE (fr.sender.userId = :userId1 AND fr.receiver.userId = :userId2 AND fr.friendRequestStatus = :status) " +
            "OR (fr.sender.userId = :userId2 AND fr.receiver.userId = :userId1 AND fr.friendRequestStatus = :status)")
    List<FriendRequest> findAllFriendRequestsBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2, @Param("status") FriendRequestStatus status);
    List<FriendRequest> findAllByReceiverUserIdAndFriendRequestStatus(Long receiverId, FriendRequestStatus status);
    List<FriendRequest> findAllBySenderUserIdAndFriendRequestStatus(Long receiverId, FriendRequestStatus status);

    void deleteBySender(User sender);
    void deleteByReceiver(User receiver);
}
