package com.mmc.bookduck.domain.book.entity;

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
public class BookInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long bookInfoId;

    @Column(unique = true)
    private String providerId; // 직접 등록한 경우 null 가능

    @NotNull
    private String title;

    @NotNull
    private String author;

    private String publisher; // null 가능

    private String publishDate; // null 가능, 연도만 제공될 수 있음

    private String description;

    private int pageCount;

    private String language; // CountryCode로 적힘

    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", updatable = false)
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Genre genre;

    @Builder
    public BookInfo(String providerId, String title, String author, String publisher, String publishDate,
                String description, int pageCount, String language, String category) {
        this.providerId = providerId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.description = description;
        this.pageCount = pageCount;
        this.language = language;
        this.category = category;
    }
}
