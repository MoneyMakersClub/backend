package com.mmc.bookduck.domain.archive.entity;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Excerpt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long excerptId;

    @NotNull
    private String excerptContent;

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @NotNull
    private Long pageNumber;

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
    public Excerpt(String excerptContent, Visibility visibility, Long pageNumber, User user, UserBook userBook) {
        this.excerptContent = excerptContent;
        this.visibility = visibility;
        this.pageNumber = pageNumber;
        this.user = user;
        this.userBook = userBook;
    }

    public void updateExcerpt(String excerptContent, Long pageNumber, Visibility visibility) {
        this.excerptContent = excerptContent;
        this.pageNumber = pageNumber;
        this.visibility = visibility;
    }

}
