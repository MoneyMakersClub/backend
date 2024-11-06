package com.mmc.bookduck.domain.onelineratingheart.entity;

import com.mmc.bookduck.domain.onelinerating.entity.OneLineRating;
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
public class OneLineRatingLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long OneLineRatingLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "one_line_rating_id", updatable = false)
    @NotNull
    private OneLineRating oneLineRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    private User user;

    @Builder
    public OneLineRatingLike (OneLineRating oneLineRating, User user) {
        this.oneLineRating = oneLineRating;
        this.user = user;
    }

    public void setOneLineRating(OneLineRating oneLineRating) {
        this.oneLineRating = oneLineRating;
    }

}
