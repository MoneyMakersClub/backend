package com.mmc.bookduck.domain.oneline.entity;

import com.mmc.bookduck.domain.book.entity.UserBook;
import com.mmc.bookduck.domain.onelineLike.entity.OneLineLike;
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
public class OneLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long oneLineId;

    @NotNull
    private String oneLineContent;

    private boolean isMain;

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

    @OneToMany(mappedBy = "oneLine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OneLineLike> oneLineLikes;

    @Builder
    public OneLine(String oneLineContent, boolean isMain, User user, UserBook userBook) {
        this.oneLineContent = oneLineContent;
        this.isMain = isMain;
        this.user = user;
        this.userBook = userBook;
        this.oneLineLikes = new ArrayList<>();
    }

    public void updateOneLine(String oneLineContent){
        this.oneLineContent = oneLineContent;
    }

    // OneLineLike 추가
    public void addOneLineLike(OneLineLike oneLineLike) {
        oneLineLikes.add(oneLineLike);
    }

    // OneLineLike 삭제
    public void removeOneLineLike(OneLineLike oneLineLike) {
        oneLineLikes.remove(oneLineLike);
        oneLineLike.setOneLine(null);
    }
}
