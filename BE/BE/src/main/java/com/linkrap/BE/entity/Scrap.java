package com.linkrap.BE.entity;

import com.linkrap.BE.dto.JoinForm;
import com.linkrap.BE.dto.ScrapChangeRequestDto;

import com.linkrap.BE.dto.ScrapDto;
import com.linkrap.BE.dto.ScrapFavoriteDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Builder
public class Scrap {
    @Id //PK
    @GeneratedValue(strategy= GenerationType.IDENTITY) //숫자 자동으로 매겨짐
    @Column(name="scrap_id")
    private Integer scrapId;

    @ManyToOne //FK
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne //FK
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name="title")
    private String scrapTitle;

    @Column(name="url")
    private String scrapLink;

    @Column(name="memo")
    private String scrapMemo;

    @Column(name="is_favorite")
    private boolean favorite;

    @Column(name="is_public")
    private boolean showPublic;

    @Column(name="created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name="updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;




    public void patch(ScrapChangeRequestDto dto) {
        if(dto.getScrapTitle()!=null)
            this.scrapTitle=dto.getScrapTitle();
        if(dto.getScrapLink()!=null)
            this.scrapLink=dto.getScrapLink();
        if(dto.getScrapMemo()!=null)
            this.scrapMemo=dto.getScrapMemo();
    }

    public void patchFavorite(ScrapFavoriteDto dto){
        this.favorite=dto.isFavorite();
    }


    public Integer getUserIdValue() {
        return user.getUserId();
    }

    public Integer getCategoryIdValue(){
        return category.getCategoryId();
    }

    public static Scrap createScrap(ScrapDto dto, Users user, Category category) {
        //예외 발생
        if (dto.getScrapId() != null)
            throw new IllegalArgumentException("스크랩 생성 실패! 스크랩의 id가 없어야 합니다.");
        //엔티티 생성 및 반환
        return new Scrap(
                dto.getScrapId(),
                user,
                category,
                dto.getScrapTitle(),
                dto.getScrapLink(),
                dto.getScrapMemo(),
                dto.isFavorite(),
                dto.isShowPublic(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

}

