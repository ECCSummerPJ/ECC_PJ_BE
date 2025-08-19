package com.linkrap.BE.service;

import com.linkrap.BE.dto.CategoryRequestDto;
import com.linkrap.BE.dto.CategoryResponseDto;
import com.linkrap.BE.entity.Category;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.CategoryRepository;
import com.linkrap.BE.repository.ScrapRepository;
import com.linkrap.BE.repository.UsersRepository;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    public CategoryResponseDto create(Integer currentUserId, @RequestBody CategoryRequestDto dto) {
        //사용자 조회
        Users loggedInUser = userRepository.findById(currentUserId).orElseThrow(()->new IllegalArgumentException("등록되지 않은 사용자입니다."));
        //글자수 공백 제한
        String trimmedCategoryName = trimAndValidateCategoryName(dto.getCategoryName());
        //중복 확인
        if (categoryRepository.existsByUser_UserIdAndCategoryName(loggedInUser.getUserId(), dto.getCategoryName()))
            throw new IllegalArgumentException("동일한 이름의 카테고리가 존재합니다.");
        //카테고리 엔티티 생성
        Category category = Category.createCategory(loggedInUser, dto);
        //카테고리 엔티티를 DB에 저장
        Category created = categoryRepository.save(category);
        //DTO로 변환해 반환
        return CategoryResponseDto.createCategoryResponseDto(created);
    }

    private String trimAndValidateCategoryName(String categoryName) {
        if (categoryName == null){
            throw new IllegalArgumentException("카테고리 이름을 입력해주세요.");
        }
        String trimmed = categoryName.trim();
        if (trimmed.isEmpty()){
            throw new IllegalArgumentException("공백만으로 이루어진 이름은 사용할 수 없습니다.");
        }
        return trimmed;
    }

    //카테고리 목록 조회
    public List<CategoryResponseDto> categories(Integer currentUserId) {
        return categoryRepository.findByUser_UserId(currentUserId)
                .stream()
                .map(CategoryResponseDto::createCategoryResponseDto)
                .collect(Collectors.toList());
    }

    //카테고리 수정
    public CategoryResponseDto update(Integer categoryId, Integer currentUserId, CategoryRequestDto dto) {
        Category target = categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        //사용자 아이디 불일치
        if (!target.getUser().getUserId().equals(currentUserId)){
            throw new IllegalStateException("카테고리를 수정할 권한이 없습니다.");
        }
        //중복 확인
        if (categoryRepository.existsByUser_UserIdAndCategoryName(currentUserId, dto.getCategoryName()))
            throw new IllegalArgumentException("동일한 이름의 카테고리가 존재합니다.");
        target.patch(dto);
        Category updated = categoryRepository.save(target);
        return CategoryResponseDto.createCategoryResponseDto(updated);
    }

    //카테고리 삭제
    public CategoryResponseDto delete(Integer categoryId, Integer currentUserId) {
        Category target = categoryRepository.findById(categoryId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 카테고리입니다."));
        //사용자 아이디 불일치
        if (!target.getUser().getUserId().equals(currentUserId)){
            throw new IllegalStateException("카테고리를 삭제할 권한이 없습니다.");
        }
        //해당 카테고리 스크랩 게시글들의 categoryId = null
        List<Scrap> scrapsToUpdate = scrapRepository.findByCategory_categoryId(categoryId);
        for (Scrap scrap : scrapsToUpdate){
            scrap.setCategory(null);
        }
        categoryRepository.delete(target);
        return CategoryResponseDto.createCategoryResponseDto(target);
    }
}
