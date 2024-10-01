package com.mmc.bookduck.domain.reviewheart.repository;

import com.mmc.bookduck.domain.reviewheart.entity.ReviewHeart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHeartRepository extends JpaRepository<ReviewHeart, Long> {
}
