package com.mmc.bookduck.domain.archive.entity;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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

    @NotNull
    private String title;

    @NotNull
    private String reviewContent;

    @NotNull
    private Visibility visibility;

    private String color;

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
    public Review(String title, String reviewContent, String color,
                  Visibility visibility, User user, UserBook userBook) {
        this.title = title;
        this.reviewContent = reviewContent;
        this.color = color;
        this.visibility = visibility;
        this.user = user;
        this.userBook = userBook;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void updateVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public void updateColor(String color) {
        this.color = color;
    }

}
