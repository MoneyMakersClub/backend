package com.mmc.bookduck.domain.excerptheart.entity;


import com.mmc.bookduck.domain.excerpt.entity.Excerpt;
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
public class ExcerptHeart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long excerptHeartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "excerpt_id", updatable = false)
    @NotNull
    private Excerpt excerpt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public ExcerptHeart (Excerpt excerpt, User user) {
        this.excerpt = excerpt;
        this.user = user;
    }

    public void setExcerpt(Excerpt excerpt) {
        this.excerpt = excerpt;
    }
}
