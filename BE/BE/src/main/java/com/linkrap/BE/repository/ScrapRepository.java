package com.linkrap.BE.repository;

import com.linkrap.BE.dto.ScrapListDto;
import com.linkrap.BE.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Integer>, JpaSpecificationExecutor<Scrap> {

    @Query("""
        select new com.linkrap.BE.dto.ScrapListDto(
            s.scrapId, s.scrapTitle, s.scrapLink, s.scrapMemo, s.favorite, s.showPublic
        )
        from Scrap s
    """)
    List<ScrapListDto> findAllScrap();

    //키워드 검색용
    @Query("""
        select new com.linkrap.BE.dto.ScrapListDto(
            s.scrapId, s.scrapTitle, s.scrapLink, s.scrapMemo, s.favorite, s.showPublic
        )
        from Scrap s
        where s.scrapTitle like concat('%', :keyword, '%')
    """)
    List<ScrapListDto> findByScrapTitleContaining(@Param("keyword") String keyword);

    //친구의 공개된 스크랩 게시글 조회
    List<Scrap> findByUser_UserIdAndShowPublicIsTrue(Integer friendUserId);
}

