package com.mmc.bookduck.domain.onelineratingheart.repository;

import com.mmc.bookduck.domain.onelinerating.entity.OneLineRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneLineRatingLikeRepository extends JpaRepository<OneLineRating, Long> {
}

