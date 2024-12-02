package com.mmc.bookduck.global.initializer;

import com.mmc.bookduck.domain.item.entity.ItemType;
import com.mmc.bookduck.domain.item.entity.ItemUnlockCondition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemData {
    PROP_01(ItemType.PROP, "책", ItemUnlockCondition.OTHERS, 0),
    PROP_02(ItemType.PROP, "아령", ItemUnlockCondition.HEALTH, 5),
    PROP_03(ItemType.PROP, "깃펜", ItemUnlockCondition.FICTION_LITERARY_HUMANITIES, 5),
    PROP_04(ItemType.PROP, "몽키스패너", ItemUnlockCondition.ARCHITECTURE_TECHNOLOGY, 5),
    PROP_05(ItemType.PROP, "노트북", ItemUnlockCondition.COMPUTER, 5),
    PROP_06(ItemType.PROP, "물감붓", ItemUnlockCondition.ART_COMICS, 5),
    PROP_07(ItemType.PROP, "세계지도", ItemUnlockCondition.TRAVEL, 5),
    PROP_08(ItemType.PROP, "플라스크", ItemUnlockCondition.SCIENCE, 5),
    HAT_01(ItemType.HAT, "헤어밴드", ItemUnlockCondition.HEALTH, 3),
    HAT_02(ItemType.HAT, "열공 머리띠", ItemUnlockCondition.SELF_HELP, 5),
    HAT_03(ItemType.HAT, "프로펠러 모자", ItemUnlockCondition.ART_HOBBY, 5),
    HAT_04(ItemType.HAT, "안전모", ItemUnlockCondition.ARCHITECTURE_TECHNOLOGY, 5),
    HAT_05(ItemType.HAT, "빵모자", ItemUnlockCondition.ART_COMICS, 5),
    HAT_06(ItemType.HAT, "요리사 모자", ItemUnlockCondition.HOME_COOKING, 5),
    HAT_07(ItemType.HAT, "익선관", ItemUnlockCondition.HISTORY, 5),
    FACE_01(ItemType.FACE, "안경", ItemUnlockCondition.FICTION_LITERARY_HUMANITIES, 3),
    FACE_02(ItemType.FACE, "순정만화 눈", ItemUnlockCondition.ART_COMICS, 5),
    CLOTHES_01(ItemType.CLOTHES, "넥타이", ItemUnlockCondition.BUSINESS_SOCIETY, 5),
    CLOTHES_02(ItemType.CLOTHES, "앞치마", ItemUnlockCondition.HOME_COOKING, 5),
    CLOTHES_03(ItemType.CLOTHES, "체크무늬 셔츠", ItemUnlockCondition.COMPUTER, 10),
    CLOTHES_04(ItemType.CLOTHES, "곤룡포", ItemUnlockCondition.HISTORY, 10),
    BAG_01(ItemType.BAG, "여행가방(백팩형)", ItemUnlockCondition.TRAVEL, 10),
    BAG_02(ItemType.BAG, "기타(크로스형)", ItemUnlockCondition.ART_HOBBY, 10),

    ;

    private final ItemType itemType;
    private final String description;
    private final ItemUnlockCondition unlockCondition;
    private final int requiredCount;
}