package com.mmc.bookduck.domain.userhome.repository;

import com.mmc.bookduck.domain.userhome.entity.CardType;
import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HomeCardRepository extends JpaRepository<HomeCard, Long> {
    List<HomeCard> findAllByUserHomeOrderByCardIndexAsc(UserHome userHome);
    List<HomeCard> findAllByUserHomeAndCardTypeAndResourceId1(UserHome userHome, CardType cardType, Long resourceId1);
    @Query("SELECT h FROM HomeCard h WHERE h.userHome = :userHome AND h.cardType IN (:cardTypes) AND (h.resourceId1 = :resourceId OR h.resourceId2 = :resourceId)")
    List<HomeCard> findAllByUserHomeAndCardTypesAndResourceId1Or2(@Param("userHome") UserHome userHome,
                                                                  @Param("cardTypes") List<CardType> cardTypes,
                                                                  @Param("resourceId") Long resourceId);
}