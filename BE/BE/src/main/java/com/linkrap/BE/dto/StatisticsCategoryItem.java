package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticsCategoryItem {
    private Integer categoryId;
    private String  categoryName;
    private Long    scrapCount;
}
