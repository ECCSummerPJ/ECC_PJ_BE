package com.linkrap.BE.service;

import com.linkrap.BE.dto.CategoryRequestDto;
import com.linkrap.BE.dto.CategoryResponseDto;
import com.linkrap.BE.dto.ScrapListDto;
import com.linkrap.BE.entity.Category;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.CategoryRepository;
import com.linkrap.BE.repository.ScrapRepository;
import com.linkrap.BE.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UsersRepository userRepository;
    @Autowired
    ScrapRepository scrapRepository;

    //카테고리 생성
    public CategoryResponseDto create(Integer userId, @RequestBody CategoryRequestDto dto) {
        //사용자 조회
        Users loggedInUser = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("등록되지 않은 사용자입니다."));
        //카테고리 엔티티 생성
        Category category = Category.createCategory(loggedInUser, dto);
        //카테고리 엔티티를 DB에 저장
        Category created = categoryRepository.save(category);
        //DTO로 변환해 반환
        return CategoryResponseDto.createCategoryDto(created);
    }

    //카테고리 목록 조회
    public List<CategoryResponseDto> categories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponseDto::createCategoryDto)
                .collect(Collectors.toList());
    }

    //카테고리 수정
    public CategoryResponseDto update(Integer categoryId, CategoryRequestDto dto) {
        Category target = categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        target.patch(dto);
        Category updated = categoryRepository.save(target);
        return CategoryResponseDto.createCategoryDto(updated);
    }

    //카테고리 삭제
    public CategoryResponseDto delete(Integer categoryId) {
        Category target = categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        categoryRepository.delete(target);
        return CategoryResponseDto.createCategoryDto(target);
    }

    //카테고리별 스크랩 목록 조회
    public List<ScrapListDto> scrapsByCategory(Integer userId, Integer categoryId) {
        return scrapRepository.findByUserIdAndCategoryId(userId, categoryId)
                .stream()
                .map(ScrapListDto::createScrapListDto)
                .collect(Collectors.toList());
    }
}
