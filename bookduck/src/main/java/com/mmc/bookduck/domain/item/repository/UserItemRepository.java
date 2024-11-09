package com.mmc.bookduck.domain.item.repository;

import com.mmc.bookduck.domain.item.entity.UserItem;
import com.mmc.bookduck.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
    List<UserItem> findAllByUserAndIsEquippedTrue(User user);
    List<UserItem> findAllByUser(User user);
}
