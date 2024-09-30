package com.mmc.bookduck.domain.book.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long genreId;

    @NotNull
    private String genreName;

    @Builder
    public Genre (String genreName) {
        this.genreName = genreName;
    }
}
