package com.mmc.bookduck.domain.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemData {
    FOUNTAIN_PEN(ItemType.PROP, "PROP_01", ItemUnlockCondition.FICTION_LITERARY_HUMANITIES_SCIENCE),
    FLASK(ItemType.PROP, "PROP_02", ItemUnlockCondition.SCIENCE),
    PAINTBRUSH(ItemType.PROP, "PROP_03", ItemUnlockCondition.ART_COMICS),
    WORLD_MAP(ItemType.PROP, "PROP_04", ItemUnlockCondition.TRAVEL),
    SPANNER(ItemType.PROP, "PROP_05", ItemUnlockCondition.ARCHITECTURE_TECHNOLOGY),
    LAPTOP(ItemType.PROP, "PROP_06", ItemUnlockCondition.COMPUTER),
    DUMBBELL(ItemType.PROP, "PROP_07", ItemUnlockCondition.HEALTH),
    BOOK(ItemType.PROP, "PROP_08", ItemUnlockCondition.OTHERS),
    BREAD_HAT(ItemType.HAT, "HAT_01", ItemUnlockCondition.ART_COMICS),
    SAFETY_HELMET(ItemType.HAT, "HAT_02", ItemUnlockCondition.ARCHITECTURE_TECHNOLOGY),
    IKSEONGWAN(ItemType.HAT, "HAT_03", ItemUnlockCondition.HISTORY),
    CHEF_HAT(ItemType.HAT, "HAT_04", ItemUnlockCondition.HOME_COOKING),
    GLASSES(ItemType.FACE, "FACE_01", ItemUnlockCondition.FICTION_LITERARY_HUMANITIES_SCIENCE),
    GONRYONGPO(ItemType.CLOTHES, "CLOTHES_01", ItemUnlockCondition.HISTORY),
    NECKTIE(ItemType.CLOTHES, "CLOTHES_02", ItemUnlockCondition.BUSINESS_SOCIETY),
    APRON(ItemType.CLOTHES, "CLOTHES_03", ItemUnlockCondition.HOME_COOKING);

    private final ItemType itemType;
    private final String itemName;
    private final ItemUnlockCondition unlockCondition;
}