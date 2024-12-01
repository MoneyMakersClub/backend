package com.mmc.bookduck.global.initializer;

import com.mmc.bookduck.domain.badge.entity.Badge;
import com.mmc.bookduck.domain.badge.repository.BadgeRepository;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.book.repository.GenreRepository;
import com.mmc.bookduck.domain.item.entity.Item;
import com.mmc.bookduck.domain.item.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class InitDB {
    private final GenreRepository genreRepository;
    private final ItemRepository itemRepository;
    private final BadgeRepository badgeRepository;

    @PostConstruct
    public void initializeGenres() {
        for (GenreName genreName : GenreName.values()) {
            if (!genreRepository.existsByGenreName(genreName)) {
                Genre genre = Genre.builder()
                        .genreName(genreName)
                        .build();
                genreRepository.save(genre);
            }
        }
    }

    @PostConstruct
    public void initializeItems() {
        for (ItemData itemData : ItemData.values()) {
            if (!itemRepository.existsByItemName(itemData.name())) {
                itemRepository.save(Item.builder()
                        .itemName(itemData.name())
                        .itemType(itemData.getItemType())
                        .description(itemData.getDescription())
                        .unlockCondition(itemData.getUnlockCondition().getGenres()
                                + "%" + itemData.getRequiredCount())
                        .build());
            }
        }
    }

    @PostConstruct
    public void initializeBadges() {
        for (BadgeData badgeData : BadgeData.values()) {
            if (!badgeRepository.existsByBadgeName(badgeData.name())) {
                String description = String.format(badgeData.getBadgeType().getDescription(), badgeData.getUnlockCondition());
                badgeRepository.save(Badge.builder()
                        .badgeName(badgeData.name())
                        .badgeType(badgeData.getBadgeType())
                        .description(description)
                        .unlockCondition(badgeData.getUnlockCondition())
                        .build());
            }
        }
    }
}

