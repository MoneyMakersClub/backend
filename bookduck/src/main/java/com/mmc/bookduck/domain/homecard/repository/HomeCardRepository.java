package com.mmc.bookduck.domain.homecard.repository;

import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.domain.homecard.entity.HomeCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeCardRepository extends JpaRepository<HomeCard, Long> {
    List<HomeCard> findAllByUserOrderByCardIndexAsc(User user);
}