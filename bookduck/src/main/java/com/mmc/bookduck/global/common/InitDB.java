package com.mmc.bookduck.global.common;

import com.mmc.bookduck.domain.badge.repository.BadgeRepository;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.book.repository.GenreRepository;
import com.mmc.bookduck.domain.item.entity.Item;
import com.mmc.bookduck.domain.item.entity.ItemData;
import com.mmc.bookduck.domain.item.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class InitDB {

    private final GenreRepository genreRepository;
    private final BadgeRepository badgeRepository;
    private final ItemRepository itemRepository;


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
                        .itemName(itemData.getItemName())
                        .itemType(itemData.getItemType())
                        .description(itemData.name())
                        .unlockCondition(itemData.getUnlockCondition().getGenres()
                                + "%" + itemData.getUnlockCondition().getRequiredCount())
                        .build());
            }
        }
    }
}

