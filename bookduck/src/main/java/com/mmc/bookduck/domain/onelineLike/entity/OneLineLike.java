package com.mmc.bookduck.domain.onelineLike.entity;

import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OneLineLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long OneLineLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "one_line_id", updatable = false)
    @NotNull
    private OneLine oneLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    private User user;

    @Builder
    public OneLineLike(OneLine oneLine, User user) {
        this.oneLine = oneLine;
        this.user = user;
    }

    public void setOneLine(OneLine oneLine) {
        this.oneLine = oneLine;
    }

}
