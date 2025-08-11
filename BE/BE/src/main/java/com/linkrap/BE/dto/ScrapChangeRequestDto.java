package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScrapChangeRequestDto {

    private String scrapTitle;
    private String scrapLink;
    private String scrapMemo;
    private boolean showPublic;

    public Scrap toEntity() {
        return Scrap.builder()
                .scrapTitle(scrapTitle)
                .scrapLink(scrapLink)
                .scrapMemo(scrapMemo)
                .showPublic(showPublic)
                .build();
    }
}
