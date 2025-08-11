package com.linkrap.BE.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scrapId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // TEXT
    @Column
    private String url;

    @Column
    private String title;

    // TEXT
    @Column
    private String memo;

    @Column
    private boolean isFavorite = false;

    @Column
    private boolean isRead = false;

    @Column
    private boolean isPublic = false;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Column
    private int viewCount = 0;
}