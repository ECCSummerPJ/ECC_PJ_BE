package com.linkrap.BE.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Builder
public class Rescrap {
    @Id //PK
    @GeneratedValue(strategy= GenerationType.IDENTITY) //숫자 자동으로 매겨짐
    @Column(name="rescrap_id") //재스크랩 id
    private Integer rescrapId;

    @ManyToOne //FK
    @JoinColumn(name="user_id")
    private Users userId;

    @ManyToOne //FK
    @JoinColumn(name="category_id")
    private Category categoryId;

    @ManyToOne //FK
    @JoinColumn(name="scrap_id") //원본 스크랩
    private Scrap scrapId;

    @Column(name="redirect_link") //리다이렉트되는 링크 (원래 스크랩으로 연결됨)
    private String redirectLink;

    @Column(name="created_at") //재스크랩 시각
    @CreationTimestamp
    private Timestamp createdAt;

    public Integer getScrapIdValue() {
        return scrapId.getScrapId();
    }
}
