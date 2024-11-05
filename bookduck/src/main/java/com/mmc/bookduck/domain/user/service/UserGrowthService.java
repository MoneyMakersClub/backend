package com.mmc.bookduck.domain.user.service;

import com.mmc.bookduck.domain.excerpt.repository.ExcerptRepository;
import com.mmc.bookduck.domain.review.repository.ReviewRepository;
import com.mmc.bookduck.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;

@Service
@RequiredArgsConstructor
public class UserGrowthService {
    private final ReviewRepository reviewRepository;
    private final ExcerptRepository excerptRepository;

    @Transactional(readOnly = true)
    public long countBookRecordsOfThisYear(User user) {
        int currentYear = Year.now().getValue();
        long reviewCount = reviewRepository.countByUserAndCreatedTimeThisYear(user, currentYear);
        long excerptCount = excerptRepository.countByUserAndCreatedTimeThisYear(user, currentYear);

        return (reviewCount + excerptCount);
    }
}
