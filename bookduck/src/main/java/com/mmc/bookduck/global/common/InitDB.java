package com.mmc.bookduck.global.common;

import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.GenreName;
import com.mmc.bookduck.domain.book.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final GenreRepository genreRepository;

    @PostConstruct
    @Transactional
    public void initializeGenres() {
        for (GenreName genreName : GenreName.values()) {
            if (!genreRepository.existsByGenreName(genreName)) {
                Genre genre = Genre.builder()
                        .genreName(genreName)
                        .build();
                genreRepository.save(genre);
            }
        }
    }
}

