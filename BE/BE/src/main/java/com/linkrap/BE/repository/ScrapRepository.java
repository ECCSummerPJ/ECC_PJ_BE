package com.linkrap.BE.repository;

import com.linkrap.BE.dto.ScrapListDto;
import com.linkrap.BE.dto.StatisticsCategoryItem;
import com.linkrap.BE.dto.StatisticsScrapItem;
import com.linkrap.BE.entity.Scrap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;


public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
    // 내 스크랩 중 조회수 순 Top N
    @Query("""
           select new com.linkrap.BE.dto.StatisticsScrapItem(s.scrapId, s.scrapTitle, s.viewCount)
           from Scrap s
           where s.authorId = :userId
           order by s.viewCount desc, s.scrapId desc
           """)
    List<StatisticsScrapItem> findTopViewedScraps(@Param("userId") int userId, Pageable pageable);

    // 내 스크랩을 카테고리로 묶어서 개수 Top N
    @Query("""
           select new com.linkrap.BE.dto.StatisticsCategoryItem(s.categoryId, c.categoryName, count(s))
           from Scrap s
             join Category c on c.categoryId = s.categoryId
           where s.authorId = :userId
           group by s.categoryId, c.categoryName
           order by count(s) desc, s.categoryId asc
           """)
    List<StatisticsCategoryItem> findTopCategoriesByUser(@Param("userId") int userId, Pageable pageable);

    List<ScrapListDto> findByUserIdAndCategoryId(Integer userId, Integer categoryId);
}

