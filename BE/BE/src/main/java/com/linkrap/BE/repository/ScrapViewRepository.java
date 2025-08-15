package com.linkrap.BE.repository;

import com.linkrap.BE.dto.StatisticsCategoryItem;
import com.linkrap.BE.dto.StatisticsScrapItem;
import com.linkrap.BE.entity.ScrapView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScrapViewRepository extends JpaRepository<ScrapView, Integer> {

    // 내 스크랩 중, 내가 열람한 횟수 기준 TOP N
    @Query("""
        select new com.linkrap.BE.dto.StatisticsScrapItem(
            s.scrapId, s.scrapTitle, count(v)
        )
        from ScrapView v
        left join v.scrapId s
        where s.user.userId = :userId
          and v.userId = :userId      
        group by s.scrapId, s.scrapTitle
        order by count(v) desc, s.scrapId desc
    """)
    List<StatisticsScrapItem> findTopViewedScrapsByOwner(
            @Param("userId") int userId, Pageable pageable
    );

    // 내 스크랩을 카테고리로 묶어, 내가 본 횟수 합계 TOP N
    @Query("""
        select new com.linkrap.BE.dto.StatisticsCategoryItem(
            c.categoryId, c.categoryName, count(v)
        )
        from ScrapView v
        left join v.scrapId s
        join s.category c
        where s.user.userId = :userId
          and v.userId = :userId
        group by c.categoryId, c.categoryName
        order by count(v) desc, c.categoryId asc
    """)
    List<StatisticsCategoryItem> findTopCategoriesByOwner(
            @Param("userId") int userId, Pageable pageable
    );
}
