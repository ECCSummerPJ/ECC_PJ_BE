package com.linkrap.BE.repository;

import com.linkrap.BE.dto.StatisticsCategoryItem;
import com.linkrap.BE.dto.StatisticsScrapItem;
import com.linkrap.BE.entity.Scrap;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ScrapRepository extends JpaRepository<Scrap, Integer> {

}

