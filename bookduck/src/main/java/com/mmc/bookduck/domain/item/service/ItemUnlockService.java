package com.mmc.bookduck.domain.item.service;

import com.mmc.bookduck.domain.alarm.service.AlarmByTypeService;
import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.book.entity.ReadStatus;
import com.mmc.bookduck.domain.book.repository.UserBookRepository;
import com.mmc.bookduck.domain.item.entity.Item;
import com.mmc.bookduck.domain.item.entity.UserItem;
import com.mmc.bookduck.domain.item.repository.ItemRepository;
import com.mmc.bookduck.domain.item.repository.UserItemRepository;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemUnlockService {
    private final UserService userService;
    private final UserBookRepository userBookRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final AlarmByTypeService alarmByTypeService;

    // 사용자가 획득할 수 있는 아이템을 UserItem으로 생성하는 메서드
    public void createUserItemForUnlockableItems(User user) {
        List<Item> unlockableItems = getUnlockableItemsForUser();

        for (Item item : unlockableItems) {
            // UserItem 생성
            UserItem userItem = UserItem.builder()
                    .user(user)
                    .item(item)
                    .isEquipped(false)  // 기본값으로 미장착 설정
                    .build();

            // UserItem 저장
            userItemRepository.save(userItem);
            // 아이템 획득 알림 생성
            alarmByTypeService.createItemUnlockedAlarm(user);
        }
    }

    // 사용자 아이템 획득 조건 확인 및 새로 획득할 수 있는 아이템 목록 반환
    public List<Item> getUnlockableItemsForUser() {
        User user = userService.getCurrentUser();

        // 사용자가 이미 획득한 아이템 목록
        List<Long> alreadyOwnedItemIds = userItemRepository.findAllByUser(user).stream()
                .map(userItem -> userItem.getItem().getItemId())
                .toList();

        // 모든 아이템 가져오기
        List<Item> allItems = itemRepository.findAll();

        // 새롭게 획득할 수 있는 아이템 필터링
        return allItems.stream()
                .filter(item -> !alreadyOwnedItemIds.contains(item.getItemId()) && isUnlockConditionMet(user, item))
                .collect(Collectors.toList());
    }

    // 특정 아이템의 언락 조건 확인
    private boolean isUnlockConditionMet(User user, Item item) {
        String[] conditionParts = item.getUnlockCondition().split("%");
        String genreNames = conditionParts[0];

        int requiredCount = 0; // 기본값을 0으로 설정
        try {
            requiredCount = Integer.parseInt(conditionParts[1]);
        } catch (NumberFormatException e) {
            log.info("item 테이블에 requireCount를 확인해주세요.");
        }

        Map<GenreName, Integer> userBookCountByGenre = getUserBookCountByGenre(user);

        String[] genreNameList = genreNames.split("\\+");
        int totalReadCount = 0;

        for (String genreName : genreNameList) {
            totalReadCount += userBookCountByGenre.getOrDefault(genreName, 0);
        }

        return totalReadCount >= requiredCount;
    }

    // 현시점 기준 사용자가 완독한 UserBok만 가져와서 장르별로 그룹화
    private Map<GenreName, Integer> getUserBookCountByGenre(User user) {
        return userBookRepository.findByUserAndReadStatus(user, ReadStatus.FINISHED).stream()
                .map(userBook -> userBook.getBookInfo().getGenre())
                .collect(Collectors.groupingBy(
                        Genre::getGenreName,
                        Collectors.summingInt(genre -> 1)
                ));
    }
}