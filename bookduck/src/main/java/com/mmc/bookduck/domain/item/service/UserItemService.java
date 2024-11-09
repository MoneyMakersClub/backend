package com.mmc.bookduck.domain.item.service;

import com.mmc.bookduck.domain.item.dto.common.ItemClosetUnitDto;
import com.mmc.bookduck.domain.item.dto.common.UserItemEquippedDto;
import com.mmc.bookduck.domain.item.dto.response.UserItemClosetResponseDto;
import com.mmc.bookduck.domain.item.dto.request.UserItemUpdateRequestDto;
import com.mmc.bookduck.domain.item.entity.Item;
import com.mmc.bookduck.domain.item.entity.ItemType;
import com.mmc.bookduck.domain.item.entity.UserItem;
import com.mmc.bookduck.domain.item.repository.UserItemRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import com.mmc.bookduck.global.exception.CustomException;
import com.mmc.bookduck.global.exception.ErrorCode;
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
                    return ItemClosetUnitDto.from(item, userItem);
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
        Map<ItemType, Long> equippedItemMap = requestDto.userItemEquipped();
        equippedItemMap.forEach((itemType, userItemId) -> {
            if (userItemId == null) {
                // userItemId가 null인 경우 DB 조회를 건너뜀
                return;
            }

            UserItem newItemToEquip = userItemRepository.findById(userItemId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USERITEM_NOT_FOUND));
            if (newItemToEquip.getItem().getItemType() != itemType) {
                throw new CustomException(ErrorCode.ITEMTYPE_MISMATCH);
            }
            newItemToEquip.updateIsEquipped(true);
        });

        // 과거 장착 아이템 해제를 저장
        userItemRepository.saveAll(equippedItems);

        // 새 아이템 장착을 저장
        equippedItemMap.values().forEach(userItemId -> {
            UserItem item = userItemRepository.findById(userItemId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USERITEM_NOT_FOUND));
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
        Map<ItemType, Long> equippedItemMap = equippedItems.stream()
                .collect(Collectors.toMap(userItem -> userItem.getItem().getItemType(), UserItem::getUserItemId));

        // 기본적으로 ItemType별로 null을 추가
        for (ItemType itemType : ItemType.values()) {
            equippedItemMap.putIfAbsent(itemType, null);
        }

        return equippedItemMap;
    }
}
