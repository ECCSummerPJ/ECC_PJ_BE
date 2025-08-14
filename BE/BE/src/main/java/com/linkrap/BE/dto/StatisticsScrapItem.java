package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticsScrapItem {
    private Integer scrapId;
    private String  title;
    private Integer viewCount;
}