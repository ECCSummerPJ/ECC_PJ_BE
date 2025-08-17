package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Scrap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RemindDto {
    private String scrapTitle;
    private String scrapMemo;

    public static RemindDto createRemindDto(Scrap scrap) {
        return new RemindDto(
                scrap.getScrapTitle(),
                scrap.getScrapMemo()
        );
    }
}
