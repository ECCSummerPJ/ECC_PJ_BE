package com.linkrap.BE.entity;

import com.linkrap.BE.dto.ScrapChangeRequestDto;
import com.linkrap.BE.dto.ScrapFavoriteDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

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
    private Users userId;

    @ManyToOne //FK
    @JoinColumn(name="category_id")
    private Category categoryId;

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
        return userId.getUserId();
    }

    public Integer getCategoryIdValue(){
        return categoryId.getCategoryId();
    }

}

