package com.linkrap.BE.repository;

import com.linkrap.BE.dto.StatisticsCategoryItem;
import com.linkrap.BE.dto.StatisticsScrapItem;
import com.linkrap.BE.entity.ScrapView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScrapViewRepository extends JpaRepository<ScrapView, Integer> {

    // 조회수 0도 포함하여 소유자 스크랩 TOP N
    @Query("""
    select new com.linkrap.BE.dto.StatisticsScrapItem(
        s.scrapId, s.scrapTitle, count(v.scrapViewId)
    )
    from Scrap s
    left join ScrapView v
        on v.scrapId.scrapId = s.scrapId
       and v.userId        = :userId
    where s.user.userId    = :userId
    group by s.scrapId, s.scrapTitle
    order by count(v.scrapViewId) desc, s.scrapId desc
""")
    List<StatisticsScrapItem> findTopViewedScrapsByOwner(@Param("userId") int userId, Pageable pageable);

    // 조회수 0도 포함하여 소유자 카테고리 TOP N
    @Query("""
    select new com.linkrap.BE.dto.StatisticsCategoryItem(
        c.categoryId, c.categoryName, count(v.scrapViewId)
    )
    from Category c
    left join Scrap s
        on s.category.categoryId = c.categoryId
       and s.user.userId         = :userId
    left join ScrapView v
        on v.scrapId.scrapId      = s.scrapId
       and v.userId               = :userId
    where c.user.userId           = :userId
    group by c.categoryId, c.categoryName
    order by count(v.scrapViewId) desc, c.categoryId asc
""")
    List<StatisticsCategoryItem> findTopCategoriesByOwner(@Param("userId") int userId, Pageable pageable);

    @Modifying
    //scrap 지워지면 해당 scrapView들도 지워지도록 하는 기능
    @Query("""
        delete
        from ScrapView sv
        where sv.scrap.scrapId=:scrapId
    """)
    void deleteScrapView(@Param("scrapId") Integer scrapId);
}
