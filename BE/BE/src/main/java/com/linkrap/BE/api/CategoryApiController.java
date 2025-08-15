package com.linkrap.BE.api;

import com.linkrap.BE.dto.CategoryRequestDto;
import com.linkrap.BE.dto.CategoryResponseDto;
import com.linkrap.BE.dto.ResponseFormat;
import com.linkrap.BE.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryApiController {
    @Autowired
    CategoryService categoryService;
    //카테고리 생성
    @PostMapping("/api/categories")
    public ResponseFormat<CategoryResponseDto> create(@RequestBody CategoryRequestDto dto) {
        int userId = 1; //임시 사용자
        CategoryResponseDto createdDto = categoryService.create(userId, dto);
        return (createdDto!=null) ?
                ResponseFormat.ok("카테고리 생성 완료", createdDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //카테고리 조회
    @GetMapping("/api/categories")
    public ResponseFormat<List<CategoryResponseDto>> categories() {
        List<CategoryResponseDto> dtos = categoryService.categories();
        return (dtos!=null) ?
                ResponseFormat.ok("카테고리 조회 완료", dtos):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //카테고리 이름 수정
    @PatchMapping("/api/categories/{categoryId}")
    public ResponseFormat<CategoryResponseDto> update(@PathVariable Integer categoryId, @RequestBody CategoryRequestDto dto){
        CategoryResponseDto updatedDto = categoryService.update(categoryId, dto);
        return (updatedDto!=null) ?
                ResponseFormat.ok("카테고리 이름 수정 완료", updatedDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //카테고리 삭제
    @DeleteMapping("/api/categories/{categoryId}")
    public ResponseFormat<CategoryResponseDto> delete(@PathVariable Integer categoryId){
        CategoryResponseDto deletedDto = categoryService.delete(categoryId);
        return (deletedDto!=null) ?
                ResponseFormat.ok("카테고리 삭제 완료", deletedDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
}
