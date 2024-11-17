package com.mmc.bookduck.domain.homecard.entity;

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

    private Long resourceId1;

    private Long resourceId2;

    private String text1;

    private String text2;

    private String text3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public HomeCard(CardType cardType, long cardIndex, Long resourceId1, Long resourceId2,
                    String text1, String text2, String text3, User user) {
        this.cardType = cardType;
        this.cardIndex = cardIndex;
        this.resourceId1 = resourceId1;
        this.resourceId2 = resourceId2;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
    }

    public void updateCardIndex(long cardIndex) {
        this.cardIndex = cardIndex;
    }
}
