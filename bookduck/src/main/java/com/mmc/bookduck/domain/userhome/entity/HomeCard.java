package com.mmc.bookduck.domain.userhome.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_home_id", updatable = false)
    @NotNull
    private UserHome userHome;

    @Builder
    public HomeCard(CardType cardType, long cardIndex, Long resourceId1, Long resourceId2,
                    String text1, String text2, UserHome userHome) {
        this.cardType = cardType;
        this.cardIndex = cardIndex;
        this.resourceId1 = resourceId1;
        this.resourceId2 = resourceId2;
        this.text1 = text1;
        this.text2 = text2;
        setUserHome(userHome);
    }

    public void updateCardIndex(long cardIndex) {
        this.cardIndex = cardIndex;
    }

    public void setUserHome(UserHome userHome) {
        this.userHome = userHome;
        if (userHome != null && !userHome.getHomeCards().contains(this)) {
            userHome.getHomeCards().add(this);
        }
    }
}
