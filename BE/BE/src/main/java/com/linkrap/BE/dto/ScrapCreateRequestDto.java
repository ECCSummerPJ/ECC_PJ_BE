package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Category;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScrapCreateRequestDto {
    private Integer userId;
    private Integer categoryId;
    private String scrapTitle;
    private String scrapLink;
    private String scrapMemo;
    private boolean showPublic;

    public Scrap toEntity() {
        return Scrap.builder()
                .userId(new Users(userId,null,null,null,null, null, null, null))
                .categoryId(new Category(categoryId,new Users(userId,null,null,null,null, null, null, null),null,null,null))
                .scrapTitle(scrapTitle)
                .scrapLink(scrapLink)
                .scrapMemo(scrapMemo)
                .showPublic(showPublic)
                .favorite(false) //초기값
                .updatedAt(null) //초기값
                .build();
    }
}
