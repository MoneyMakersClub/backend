package com.mmc.bookduck.domain.book.entity;

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
public class UserBook extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long userBookId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ReadStatus readStatus;

    @ColumnDefault("false")
    private boolean isExcerptExpGiven;

    @ColumnDefault("false")
    private boolean isReviewExpGiven;

    @ColumnDefault("false")
    private boolean isOlrExpGiven;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE) // 다대일 단방향이므로 설정
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_info_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BookInfo bookInfo;

    @Builder
    public UserBook(ReadStatus readStatus, User user, BookInfo bookInfo) {
        this.readStatus = readStatus;
        this.user = user;
        this.bookInfo = bookInfo;
        this.isExcerptExpGiven = false;
        this.isReviewExpGiven = false;
        this.isOlrExpGiven = false;
    }

    public void changeReadStatus(ReadStatus readStatus) {
        this.readStatus = readStatus;
    }

    // Excerpt 경험치 상태 변경
    public void markExcerptExpGiven() {
        this.isExcerptExpGiven = true;
    }

    // Review 경험치 상태 변경
    public void markReviewExpGiven() {
        this.isReviewExpGiven = true;
    }

    // Olr 경험치 상태 변경
    public void markOlrExpGiven() {
        this.isOlrExpGiven = true;
    }
}