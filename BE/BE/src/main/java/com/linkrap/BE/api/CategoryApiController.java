package com.linkrap.BE.api;

import com.linkrap.BE.dto.CategoryRequestDto;
import com.linkrap.BE.dto.CategoryResponseDto;
import com.linkrap.BE.dto.ResponseFormat;
import com.linkrap.BE.dto.ScrapListDto;
import com.linkrap.BE.service.CategoryService;
import com.linkrap.BE.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "카테고리 API")
public class CategoryApiController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ScrapService scrapService;
    //카테고리 생성
    @Operation(summary = "카테고리 생성", description = "카테고리 이름 설정 가능")
    @PostMapping("/categories")
    public ResponseFormat<CategoryResponseDto> create(@RequestBody CategoryRequestDto dto) {
        int userId = 1; //임시 사용자
        CategoryResponseDto createdDto = categoryService.create(userId, dto);
        return (createdDto!=null) ?
                ResponseFormat.ok("카테고리 생성 완료", createdDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //카테고리 조회
    @Operation(summary = "카테고리 목록 조회", description = "로그인한 사용자가 만든 카테고리 목록 반환")
    @GetMapping("/categories")
    public ResponseFormat<List<CategoryResponseDto>> categories() {
        Integer userId = 1; //임시 사용자
        List<CategoryResponseDto> dtos = categoryService.categories(userId);
        return (dtos!=null) ?
                ResponseFormat.ok("카테고리 조회 완료", dtos):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //카테고리 이름 수정
    @Operation(summary = "카테고리 이름 수정", description = "경로변수로 받은 ID의 카테고리 이름 변경")
    @PatchMapping("/categories/{categoryId}")
    public ResponseFormat<CategoryResponseDto> update(@PathVariable Integer categoryId, @RequestBody CategoryRequestDto dto){
        CategoryResponseDto updatedDto = categoryService.update(categoryId, dto);
        return (updatedDto!=null) ?
                ResponseFormat.ok("카테고리 이름 수정 완료", updatedDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //카테고리 삭제
    @Operation(summary = "카테고리 삭제", description = "경로변수로 받은 ID의 카테고리 삭제")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseFormat<CategoryResponseDto> delete(@PathVariable Integer categoryId){
        CategoryResponseDto deletedDto = categoryService.delete(categoryId);
        return (deletedDto!=null) ?
                ResponseFormat.ok("카테고리 삭제 완료", deletedDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //카테고리별 스크랩 목록
    @Operation(summary = "카테고리별 스크랩 목록", description = "경로변수로 받은 ID의 카테고리로 설정된 스크랩 게시글 목록 반환")
    @GetMapping("/categories/{categoryId}/scraps")
    public ResponseFormat<List<ScrapListDto>> getScraps(@PathVariable Integer categoryId,
                                                                 @RequestParam(required=false) Boolean favorite,
                                                                 @RequestParam(required=false) Boolean showPublic){
        Integer userId = 1; //임시 사용자
        List<ScrapListDto> dtos = scrapService.getScrapsByFilter(userId, categoryId, favorite, showPublic);
        return (dtos!=null) ?
                ResponseFormat.ok("해당 카테고리의 스크랩 조회 완료", dtos):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
}
