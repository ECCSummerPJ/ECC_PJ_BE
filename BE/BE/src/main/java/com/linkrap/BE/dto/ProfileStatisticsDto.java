package com.linkrap.BE.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProfileStatisticsDto {
    private List<StatisticsScrapItem> mostViewedScraps;
    private List<StatisticsCategoryItem> mostScrappedCategories;
}
