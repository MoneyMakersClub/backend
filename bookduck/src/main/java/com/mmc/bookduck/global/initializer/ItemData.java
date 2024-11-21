package com.mmc.bookduck.global.initializer;

import com.mmc.bookduck.domain.item.entity.ItemType;
import com.mmc.bookduck.domain.item.entity.ItemUnlockCondition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemData {
    PROP_01(ItemType.PROP, "깃펜", ItemUnlockCondition.FICTION_LITERARY_HUMANITIES_SCIENCE),
    PROP_02(ItemType.PROP, "플라스크", ItemUnlockCondition.SCIENCE),
    PROP_03(ItemType.PROP, "물감붓", ItemUnlockCondition.ART_COMICS),
    PROP_04(ItemType.PROP, "세계지도", ItemUnlockCondition.TRAVEL),
    PROP_05(ItemType.PROP, "몽키스패너", ItemUnlockCondition.ARCHITECTURE_TECHNOLOGY),
    PROP_06(ItemType.PROP, "노트북", ItemUnlockCondition.COMPUTER),
    PROP_07(ItemType.PROP, "아령", ItemUnlockCondition.HEALTH),
    PROP_08(ItemType.PROP, "책", ItemUnlockCondition.OTHERS),
    HAT_01(ItemType.HAT, "빵모자", ItemUnlockCondition.ART_COMICS),
    HAT_02(ItemType.HAT, "안전모", ItemUnlockCondition.ARCHITECTURE_TECHNOLOGY),
    HAT_03(ItemType.HAT, "익선관", ItemUnlockCondition.HISTORY),
    HAT_04(ItemType.HAT, "요리사 모자", ItemUnlockCondition.HOME_COOKING),
    FACE_01(ItemType.FACE, "안경", ItemUnlockCondition.FICTION_LITERARY_HUMANITIES_SCIENCE),
    CLOTHES_01(ItemType.CLOTHES, "곤룡포", ItemUnlockCondition.HISTORY),
    CLOTHES_02(ItemType.CLOTHES, "넥타이", ItemUnlockCondition.BUSINESS_SOCIETY),
    CLOTHES_03(ItemType.CLOTHES, "앞치마", ItemUnlockCondition.HOME_COOKING);

    private final ItemType itemType;
    private final String description;
    private final ItemUnlockCondition unlockCondition;
}