package com.mmc.bookduck.domain.item.service;

import com.mmc.bookduck.domain.item.dto.common.ItemClosetUnitDto;
import com.mmc.bookduck.domain.item.dto.common.ItemEquippedUnitDto;
import com.mmc.bookduck.domain.item.dto.response.UserItemEquippedResponseDto;
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

    // userId로 장착된 아이템 조회
    @Transactional(readOnly = true)
    public UserItemEquippedResponseDto getEquippedItemsOfUserByUserId(Long userId) {
        User user = userService.getActiveUserByUserId(userId);
        return new UserItemEquippedResponseDto(getUserItemEquippedListOfUser(user));
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

        return new UserItemClosetResponseDto(itemList);
    }

    public void updateUserItemsEquippedStatus(UserItemUpdateRequestDto requestDto) {
        User user = userService.getCurrentUser();

        // 현재 장착된 아이템들을 해제
        List<UserItem> equippedItems = userItemRepository.findAllByUserAndIsEquippedTrue(user);
        equippedItems.forEach(item -> item.updateIsEquipped(false));
        userItemRepository.saveAll(equippedItems);

        // 새로 장착할 아이템 처리
        Map<ItemType, Long> equippedItemMap = requestDto.equippedUserItemIdList();
        equippedItemMap.forEach((itemType, userItemId) -> {
            if (userItemId != null) { // userItemId가 null인 경우 건너뜀
                UserItem newItemToEquip = userItemRepository.findById(userItemId)
                        .orElseThrow(() -> new CustomException(ErrorCode.USERITEM_NOT_FOUND));
                if (!newItemToEquip.getUser().equals(user)) {
                    throw new CustomException(ErrorCode.USERITEM_BAD_REQUEST);
                }
                if (newItemToEquip.getItem().getItemType() != itemType) {
                    throw new CustomException(ErrorCode.ITEMTYPE_MISMATCH);
                }
                newItemToEquip.updateIsEquipped(true);
                userItemRepository.save(newItemToEquip);
            }
        });
    }

    @Transactional(readOnly = true)
    public List<UserItem> getAllUserItemsByUser(User user) {
        return userItemRepository.findAllByUser(user);
    }

    @Transactional(readOnly = true)
    public List<UserItem> getAllUserItemsByUserAndIsEquippedTrue(User user) {
        return userItemRepository.findAllByUserAndIsEquippedTrue(user);
    }

    @Transactional(readOnly = true)
    public List<ItemEquippedUnitDto> getUserItemEquippedListOfUser(User user) {
        List<UserItem> equippedItems = getAllUserItemsByUserAndIsEquippedTrue(user);

        // 장착된 아이템을 ItemType을 키로 하는 Map으로 변환
        Map<ItemType, UserItem> equippedItemMap = equippedItems.stream()
                .collect(Collectors.toMap(userItem -> userItem.getItem().getItemType(), userItem -> userItem));

        // 모든 ItemType에 대해 기본값을 설정 (없으면 null)
        for (ItemType itemType : ItemType.values()) {
            equippedItemMap.putIfAbsent(itemType, null);
        }

        return equippedItemMap.entrySet().stream()
                .map(entry -> new ItemEquippedUnitDto(entry.getKey(), entry.getValue() != null ? entry.getValue().getItem().getItemName() : null))
                .collect(Collectors.toList());
    }

    public void deletUserItemsByUser(User user) {
        userItemRepository.deleteAllByUser(user);
    }
}

