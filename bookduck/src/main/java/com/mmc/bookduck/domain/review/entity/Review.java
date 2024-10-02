package com.mmc.bookduck.domain.review.entity;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.reviewheart.entity.ReviewHeart;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long reviewId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    private Long pageNumber;

    @ColumnDefault("false")
    private boolean isMain;

    @NotNull
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE) // 다대일 단방향이므로 설정
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_book_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE) // 다대일 단방향이므로 설정
    private UserBook userBook;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewHeart> reviewHearts;

    @Builder
    public Review(String title, String content, Long pageNumber, boolean isMain,
                  Visibility visibility, User user, UserBook userBook) {
        this.title = title;
        this.content = content;
        this.pageNumber = pageNumber;
        this.isMain = isMain;
        this.visibility = visibility;
        this.user = user;
        this.userBook = userBook;
        this.reviewHearts = new ArrayList<>();
    }

    // reviewHeart 추가
    public void addReviewHeart(ReviewHeart reviewHeart) {
        reviewHearts.add(reviewHeart);
    }

    // reviewHeart 삭제
    public void removeReviewHeart(ReviewHeart reviewHeart) {
        reviewHearts.remove(reviewHeart);
        reviewHeart.setReview(null);
    }
}
