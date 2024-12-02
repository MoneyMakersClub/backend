package com.mmc.bookduck.domain.item.repository;

import com.mmc.bookduck.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByItemName(String itemName);
    Optional<Item> findByUnlockCondition(String unlockCondition);
}
