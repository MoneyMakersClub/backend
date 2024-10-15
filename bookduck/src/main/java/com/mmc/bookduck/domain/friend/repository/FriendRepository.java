package com.mmc.bookduck.domain.friend.repository;

import com.mmc.bookduck.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Optional<Friend> findByUser1IdAndUser2Id(Long userId, Long userId1);
    List<Friend> findByUser1Id(Long user1Id);
}
