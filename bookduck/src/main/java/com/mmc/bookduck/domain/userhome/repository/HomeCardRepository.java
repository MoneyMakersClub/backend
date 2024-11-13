package com.mmc.bookduck.domain.userhome.repository;

import com.mmc.bookduck.domain.userhome.entity.HomeCard;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeCardRepository extends JpaRepository<HomeCard, Long> {
    List<HomeCard> findAllByUserHome(UserHome userHome);
}
