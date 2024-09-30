package com.mmc.bookduck.domain.skin.entity;

import com.mmc.bookduck.domain.badge.entity.UserBadge;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Skin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long skinId;

    @NotNull
    private String skinName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SkinType skinType;

    @NotNull
    private String description;

    @NotNull
    private String unlockCondition; // 추후 수정 필요할 수 있음

    @NotNull
    private LocalDate releaseDate;

    @Builder
    public Skin(String skinName, SkinType skinType, String description, String unlockCondition, LocalDate releaseDate) {
        this.skinName = skinName;
        this.skinType = skinType;
        this.description = description;
        this.unlockCondition = unlockCondition;
        this.releaseDate = releaseDate;
    }
}
