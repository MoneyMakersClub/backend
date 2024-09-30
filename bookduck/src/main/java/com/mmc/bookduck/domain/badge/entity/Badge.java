package com.mmc.bookduck.domain.badge.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long badgeId;

    @NotNull
    private String badgeName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private BadgeType badgeType;

    @NotNull
    private String description;

    @NotNull
    private String condition; // 추후 수정 필요할 수 있음

    @NotNull
    private LocalDate releaseDate;

    @Builder
    public Badge(String badgeName, BadgeType badgeType, String description, String condition, LocalDate releaseDate) {
        this.badgeName = badgeName;
        this.badgeType = badgeType;
        this.description = description;
        this.condition = condition;
        this.releaseDate = releaseDate;
    }
}