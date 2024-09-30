package com.mmc.bookduck.domain.character.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long characterId;

    @NotNull
    private String characterName;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CharacterType characterType;

    @NotNull
    private String description;

    @NotNull
    private String condition; // 추후 수정 필요할 수 있음

    @NotNull
    private LocalDate releaseDate;

    @Builder
    public Character(String characterName, CharacterType characterType, String description, String condition, LocalDate releaseDate) {
        this.characterName = characterName;
        this.characterType = characterType;
        this.description = description;
        this.condition = condition;
        this.releaseDate = releaseDate;
    }
}
