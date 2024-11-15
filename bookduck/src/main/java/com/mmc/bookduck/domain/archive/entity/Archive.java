package com.mmc.bookduck.domain.archive.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Archive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long archiveId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "excerpt_id")
    private Excerpt excerpt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id")
    private Review review;

    @Builder
    public Archive(Excerpt excerpt, Review review) {
        this.excerpt = excerpt;
        this.review = review;
    }

    public void updateArchive(Excerpt updatedExcerpt, Review updatedReview) {
        this.excerpt = updatedExcerpt;
        this.review = updatedReview;
    }

    public void updateExcerpt(Excerpt newExcerpt) {
        this.excerpt = newExcerpt;
    }

    public void updateReview(Review newReview) {
        this.review = newReview;
    }
}
