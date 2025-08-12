package com.linkrap.BE.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users authorId;

    @ManyToOne
    @JoinColumn(name="scrap_id")
    private Scrap scrapId;

    @Column
    private String content;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
