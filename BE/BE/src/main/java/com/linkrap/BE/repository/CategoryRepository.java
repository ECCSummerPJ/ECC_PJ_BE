package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByUser_UserId(Integer userId);
}
