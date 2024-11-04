package com.mmc.bookduck.domain.archive.repository;

import com.mmc.bookduck.domain.archive.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
