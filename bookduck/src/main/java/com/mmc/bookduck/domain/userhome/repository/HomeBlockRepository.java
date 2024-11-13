package com.mmc.bookduck.domain.userhome.repository;

import com.mmc.bookduck.domain.userhome.entity.HomeBlock;
import com.mmc.bookduck.domain.userhome.entity.UserHome;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeBlockRepository extends JpaRepository<HomeBlock, Long> {
    List<HomeBlock> findAllByUserHome(UserHome userHome);
}
