package com.mmc.bookduck.domain.onelinerating.entity;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.onelineratingheart.entity.OneLineRatingLike;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OneLineRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long oneLineRatingId;

    @NotNull
    private String oneLineContent;

    @NotNull
    private double rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToOne(fetch = FetchType.LAZY) // 일대일
    @JoinColumn(name = "user_book_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserBook userBook;

    @OneToMany(mappedBy = "oneLineRating", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OneLineRatingLike> oneLineRatingLikes;

    @Builder
    public OneLineRating(String oneLineContent, double rating, User user, UserBook userBook) {
        this.oneLineContent = oneLineContent;
        this.rating = rating;
        this.user = user;
        this.userBook = userBook;
        this.oneLineRatingLikes = new ArrayList<>();
    }

    // OneLineRatingLike 추가
    public void addOneLineRatingLike(OneLineRatingLike oneLineRatingLike) {
        oneLineRatingLikes.add(oneLineRatingLike);
    }

    // OneLineRatingLike 삭제
    public void removeOneLineRatingLike(OneLineRatingLike oneLineRatingLike) {
        oneLineRatingLikes.remove(oneLineRatingLike);
        oneLineRatingLike.setOneLineRating(null);
    }
}
