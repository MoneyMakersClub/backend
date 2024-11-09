package com.mmc.bookduck.domain.item.service;

import com.mmc.bookduck.domain.item.dto.common.ItemClosetUnitDto;
import com.mmc.bookduck.domain.item.dto.common.UserItemEquippedDto;
import com.mmc.bookduck.domain.item.dto.request.UserItemClosetResponseDto;
import com.mmc.bookduck.domain.item.dto.request.UserItemUpdateRequestDto;
import com.mmc.bookduck.domain.item.entity.Item;
import com.mmc.bookduck.domain.item.entity.ItemType;
import com.mmc.bookduck.domain.item.entity.UserItem;
import com.mmc.bookduck.domain.item.repository.UserItemRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserItemService {
    private final UserItemRepository userItemRepository;
    private final UserService userService;
    private final ItemService itemService;

    // userId로 장착된 스킨 조회
    @Transactional(readOnly = true)
    public UserItemEquippedDto getEquippedItemsOfUserByUserId(Long userId) {
        User user = userService.getUserByUserId(userId);
        return new UserItemEquippedDto(getEquippedItemOfUserMap(user));
    }

    // user로 장착된 스킨 조회
    @Transactional(readOnly = true)
    public UserItemEquippedDto getEquippedItemsOfUser(User user) {
        return new UserItemEquippedDto(getEquippedItemOfUserMap(user));
    }

    @Transactional(readOnly = true)
    public UserItemClosetResponseDto getUserItemCloset() {
        User user = userService.getCurrentUser();
        List<Item> allItems = itemService.getAllItems();
        List<UserItem> ownedItems = getAllUserItemsByUser(user);
        Map<Long, UserItem> ownedUserItemMap = ownedItems.stream()
                .collect(Collectors.toMap(userItem -> userItem.getItem().getItemId(), userItem -> userItem));

        // 모든 아이템을 isOwned와 isEquipped 상태를 표시하여 DTO에 매핑
        List<ItemClosetUnitDto> itemList = allItems.stream()
                .map(item -> {
                    UserItem userItem = ownedUserItemMap.get(item.getItemId());
                    Boolean isOwned = (userItem != null);
                    Boolean isEquipped = (userItem != null && userItem.isEquipped());
                    return ItemClosetUnitDto.from(item, isOwned, isEquipped);
                })
                .collect(Collectors.toList());

        return new UserItemClosetResponseDto(getEquippedItemOfUserMap(user), itemList);
    }

    public void updateUserItemsEquippedStatus(UserItemUpdateRequestDto requestDto) {
        User user = userService.getCurrentUser();

        // 과거 장착 아이템 해제
        List<UserItem> equippedItems = userItemRepository.findAllByUserAndIsEquippedTrue(user);
        equippedItems.forEach(item -> item.updateIsEquipped(false));

        // 새 아이템 장착
        Map<ItemType, Long> equippedItemMap = requestDto.equippedItems();
        equippedItemMap.forEach((itemType, userItemId) -> {
            UserItem newItemToEquip = userItemRepository.findById(userItemId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid userItemId: " + userItemId));
            if (newItemToEquip.getItem().getItemType() != itemType) {
                throw new IllegalArgumentException("ItemType mismatch for userItemId: " + userItemId);
            }
            newItemToEquip.updateIsEquipped(true);
        });

        // 과거 장착 아이템 해제를 저장
        userItemRepository.saveAll(equippedItems);

        // 새 아이템 장착을 저장
        equippedItemMap.values().forEach(userItemId -> {
            UserItem item = userItemRepository.findById(userItemId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid userItemId: " + userItemId));
            userItemRepository.save(item);
        });
    }

    @Transactional(readOnly = true)
    public List<UserItem> getAllUserItemsByUser(User user) {
        return userItemRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public List<UserItem> getAllUserItemsByUserAndIsEquippedTrue(User user) {
        return userItemRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public Map<ItemType, Long> getEquippedItemOfUserMap(User user) {
        List<UserItem> equippedItems = getAllUserItemsByUserAndIsEquippedTrue(user);
        return equippedItems.stream()
                .collect(Collectors.toMap(userItem -> userItem.getItem().getItemType(), UserItem::getUserItemId));
    }
}
