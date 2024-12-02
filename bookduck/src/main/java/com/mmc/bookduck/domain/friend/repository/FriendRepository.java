package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.Friend;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT f FROM Friend f WHERE (f.user1.userId = :senderId AND f.user2.userId = :receiverId) " +
            "OR (f.user1.userId = :receiverId AND f.user2.userId = :senderId)")
    Optional<Friend> findFriendBetweenUsers(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
    List<Friend> findAllByUser1UserIdOrUser2UserId(Long userId1, Long userId2);

    void deleteByUser1(User user);
    void deleteByUser2(User user);
}
