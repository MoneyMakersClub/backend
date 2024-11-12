package com.mmc.bookduck.domain.onelineLike.repository;

import com.mmc.bookduck.domain.oneline.entity.OneLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneLineLikeRepository extends JpaRepository<OneLine, Long> {
}

