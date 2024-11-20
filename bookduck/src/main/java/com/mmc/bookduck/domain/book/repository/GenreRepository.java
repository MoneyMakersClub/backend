package com.mmc.bookduck.domain.book.repository;

import com.mmc.bookduck.domain.book.entity.Genre;
import com.mmc.bookduck.domain.book.entity.GenreName;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    Optional<Genre> findByGenreName(GenreName genreName);

    boolean existsByGenreName(GenreName genreName);
}
