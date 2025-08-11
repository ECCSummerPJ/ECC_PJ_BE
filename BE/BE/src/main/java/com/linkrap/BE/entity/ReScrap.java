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
public class ReScrap {
    @Id //PK
    @GeneratedValue(strategy= GenerationType.IDENTITY) //숫자 자동으로 매겨짐
    @Column(name="rescrap_id") //재스크랩 id
    private Integer rescrapId;
    @Column(name="author_id")  //리스크랩 하는 사람
    private Integer authorId;
    @Column(name="category_id")
    private Integer categoryId;
    @Column(name="scrap_id") //원본 스크랩
    private Integer scrapId;
    @Column(name="created_at") //재스크랩 시각
    @CreationTimestamp
    private Timestamp createdAt;
}
