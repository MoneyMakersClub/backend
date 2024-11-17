package com.mmc.bookduck.domain.homecard.repository;

import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.homecard.entity.CardType;
import com.mmc.bookduck.domain.homecard.entity.HomeCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeCardRepository extends JpaRepository<HomeCard, Long> {
    List<HomeCard> findAllByUserOrderByCardIndexAsc(User user);
    List<HomeCard> findAllByUserAndVisibilityByCardIndexAsc(User user, Visibility visibility);
    List<HomeCard> findAllByCardTypeAndResourceId1(CardType cardType, Long resourceId1);
    @Query("SELECT h FROM HomeCard h WHERE h.user = :user AND h.cardType IN (:cardTypes) AND (h.resourceId1 = :resourceId OR h.resourceId2 = :resourceId)")
    List<HomeCard> findAllByCardTypesAndResourceId1Or2(@Param("cardTypes") List<CardType> cardTypes,
                                                       @Param("resourceId") Long resourceId);
}