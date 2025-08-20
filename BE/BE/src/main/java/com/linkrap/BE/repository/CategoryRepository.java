package com.linkrap.BE.repository;

import com.linkrap.BE.dto.CategoryDto;
import com.linkrap.BE.entity.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByUser_UserId(Integer userId);

    boolean existsByUser_UserIdAndCategoryName(int userId, String categoryName);

    @Query("""
        select c.categoryName
        from Category c
        where c.categoryId=:categoryId
    """)
    String findByCategoryId(Integer categoryId);

    @Query("""
        select c.categoryId
        from Category c
        where c.categoryName=:categoryName
    """)
    Integer findByCategoryName(String categoryName);
}
