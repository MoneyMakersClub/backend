package com.mmc.bookduck.domain.homecard.entity;

import com.mmc.bookduck.domain.archive.entity.Excerpt;
import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.oneline.entity.OneLine;
import com.mmc.bookduck.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class HomeCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long homeCardId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CardType cardType;

    @NotNull
    private Long cardIndex;

    private String text1;

    private String text2;

    private String text3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excerpt_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Excerpt excerpt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "one_line_id", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private OneLine oneLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_book_id1", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserBook userBook1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_book_id2", updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserBook userBook2;

    @Builder
    public HomeCard(CardType cardType, long cardIndex, User user,
                    String text1, String text2, String text3,
                    Excerpt excerpt, OneLine oneLine, UserBook userBook1, UserBook userBook2) {
        this.cardType = cardType;
        this.cardIndex = cardIndex;
        this.user = user;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.excerpt = excerpt;
        this.oneLine = oneLine;
        this.userBook1 = userBook1;
        this.userBook2 = userBook2;
    }

    public void updateCardIndex(long cardIndex) {
        this.cardIndex = cardIndex;
    }

    public void setExcerpt(Excerpt excerpt) {
        this.excerpt = excerpt;
    }

    public void setOneLine(OneLine oneLine) {
        this.oneLine = oneLine;
    }

    public void setUserBook(UserBook userBook1, UserBook userBook2) {
        this.userBook1 = userBook1;
        this.userBook2 = userBook2;
    }
}
