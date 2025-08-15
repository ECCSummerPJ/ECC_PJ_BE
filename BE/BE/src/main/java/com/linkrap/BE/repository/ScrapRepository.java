package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Integer>, JpaSpecificationExecutor<Scrap> {

    //키워드 검색용
    List<Scrap> findByScrapTitleContaining(String keyword);

}

