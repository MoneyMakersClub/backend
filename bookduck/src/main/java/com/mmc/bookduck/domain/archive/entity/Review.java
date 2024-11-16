package com.mmc.bookduck.domain.archive.entity;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long reviewId;

    @Size(max = 25, message = "리뷰 제목은 최대 25자까지 입력할 수 있습니다.")
    private String reviewTitle;

    @NotNull
    @Size(max = 1000, message = "리뷰 내용은 최대 1000자까지 입력할 수 있습니다.")
    private String reviewContent;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    private String color;

    private boolean isSystemGenerated;

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

    @Builder
    public Review(String reviewTitle, String reviewContent, String color,
                  Visibility visibility, User user, UserBook userBook) {
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.color = color;
        this.visibility = visibility;
        this.user = user;
        this.userBook = userBook;
    }

    public void updateReview(String reviewTitle, String reviewContent, String color, Visibility visibility) {
        this.reviewTitle = reviewTitle;
        this.reviewContent = reviewContent;
        this.color = color;
        this.visibility = visibility;
    }

    public void setReviewTitle(String reviewTitle) {this.reviewTitle = reviewTitle;}

    public void setIsSystemGenerated(boolean isSystemGenerated) {this.isSystemGenerated = isSystemGenerated;}



}
