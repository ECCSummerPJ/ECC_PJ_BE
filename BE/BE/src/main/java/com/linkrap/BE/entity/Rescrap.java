package com.linkrap.BE.entity;

import com.linkrap.BE.dto.RescrapDto;
import com.linkrap.BE.dto.ScrapDto;
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

    public static Rescrap createRescrap(RescrapDto dto, Scrap scrap, Users user, Category category) {
        //예외 발생
        if (dto.getRescrapId() != null)
            throw new IllegalArgumentException("스크랩 생성 실패! 리스크랩의 id가 없어야 합니다.");
        //엔티티 생성 및 반환
        return new Rescrap(
                dto.getRescrapId(),
                user,
                category,
                scrap,
                dto.getRedirectLink(),
                dto.getCreatedAt()
        );
    }
}
