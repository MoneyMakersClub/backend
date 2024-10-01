package com.mmc.bookduck.domain.excerpt.entity;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.common.Visibility;
import com.mmc.bookduck.domain.excerptheart.entity.ExcerptHeart;
import com.mmc.bookduck.domain.user.entity.User;
import com.mmc.bookduck.global.common.BaseTimeEntity;
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
public class Excerpt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long excerptId;

    @NotNull
    private String excerptContent;

    @NotNull
    private Visibility visibility;

    private Long pageNumber;

    @NotNull
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

    @OneToMany(mappedBy = "excerpt", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExcerptHeart> excerptHearts;

    @Builder
    public Excerpt(String excerptContent, Visibility visibility,
                   Long pageNumber, String color, User user, UserBook userBook) {
        this.excerptContent = excerptContent;
        this.visibility = visibility;
        this.pageNumber = pageNumber;
        this.color = color;
        this.user = user;
        this.userBook = userBook;
        this.excerptHearts = new ArrayList<>();
    }

    // excerptHeart 추가
    public void addExcerptHeart(ExcerptHeart excerptHeart) {
        excerptHearts.add(excerptHeart);
    }

    // excerptHeart 삭제
    public void removeExcerptHeart(ExcerptHeart excerptHeart) {
        excerptHearts.remove(excerptHeart);
        excerptHeart.setExcerpt(null);
    }
}
