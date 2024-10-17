package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT f FROM Friend f WHERE (f.user1.userId = :userId1 AND f.user2.userId = :userId2) " +
            "OR (f.user1.userId = :userId2 AND f.user2.userId = :userId1)")
    Optional<Friend> findFriendBetweenUsers(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
    ;
    List<Friend> findByUser1UserId(Long user1Id);
}
