package com.linkrap.BE.api;

import com.linkrap.BE.dto.CategoryRequestDto;
import com.linkrap.BE.dto.CategoryResponseDto;
import com.linkrap.BE.dto.ScrapListDto;
import com.linkrap.BE.security.CustomUserDetails;
import com.linkrap.BE.service.CategoryService;
import com.linkrap.BE.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "카테고리 API")
@SecurityRequirement(name="bearerAuth")
public class CategoryApiController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ScrapService scrapService;
    //카테고리 생성
    @Operation(summary = "카테고리 생성", description = "카테고리 이름 설정 가능")
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDto> create(@RequestBody CategoryRequestDto dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer currentUserId = userDetails.getUserId();
        CategoryResponseDto createdDto = categoryService.create(currentUserId, dto);
        return (createdDto!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdDto) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //카테고리 조회
    @Operation(summary = "카테고리 목록 조회", description = "로그인한 사용자가 만든 카테고리 목록 반환")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> categories(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer currentUserId = userDetails.getUserId();
        List<CategoryResponseDto> dtos = categoryService.categories(currentUserId);
        return (dtos!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(dtos) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //카테고리 이름 수정
    @Operation(summary = "카테고리 이름 수정", description = "경로변수로 받은 ID의 카테고리 이름 변경")
    @PatchMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Integer categoryId, @RequestBody CategoryRequestDto dto,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        Integer currentUserId = userDetails.getUserId();
        CategoryResponseDto updatedDto = categoryService.update(categoryId, currentUserId, dto);
        return (updatedDto!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(updatedDto) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //카테고리 삭제
    @Operation(summary = "카테고리 삭제", description = "경로변수로 받은 ID의 카테고리 삭제")
    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> delete(@PathVariable Integer categoryId,
                                                      @AuthenticationPrincipal CustomUserDetails userDetails){
        Integer currentUserId = userDetails.getUserId();
        CategoryResponseDto deletedDto = categoryService.delete(categoryId, currentUserId);
        return (deletedDto!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(deletedDto) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    //카테고리별 스크랩 목록
    @Operation(summary = "카테고리별 스크랩 목록", description = "경로변수로 받은 ID의 카테고리로 설정된 스크랩 게시글 목록 반환")
    @GetMapping("/categories/{categoryId}/scraps")
    public ResponseEntity<List<ScrapListDto>> getScraps(@PathVariable Integer categoryId,
                                                        @RequestParam(required=false) Boolean favorite,
                                                        @RequestParam(required=false) Boolean showPublic,
                                                        @AuthenticationPrincipal CustomUserDetails userDetails){
        Integer currentUserId = userDetails.getUserId();
        List<ScrapListDto> dtos = scrapService.getScrapsByFilter(currentUserId, categoryId, favorite, showPublic);
            return (dtos!=null) ?
                    ResponseEntity.status(HttpStatus.OK).body(dtos) :
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
