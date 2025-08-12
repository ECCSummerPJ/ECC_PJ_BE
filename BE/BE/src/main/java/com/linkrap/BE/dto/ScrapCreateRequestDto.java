package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScrapCreateRequestDto {
    private Integer authorId;
    private Integer categoryId;
    private String scrapTitle;
    private String scrapLink;
    private String scrapMemo;
    private boolean showPublic;

    public Scrap toEntity() {
        return Scrap.builder()
                .authorId(authorId)
                .categoryId(categoryId)
                .scrapTitle(scrapTitle)
                .scrapLink(scrapLink)
                .scrapMemo(scrapMemo)
                .showPublic(showPublic)
                .favorite(false) //초기값
                .build();
    }
}
