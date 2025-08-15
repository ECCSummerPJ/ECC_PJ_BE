package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ScrapController {

    @Autowired
    private ScrapService scrapService;

    //스크랩 생성
    @Operation(summary = "스크랩 생성")
    @PostMapping("/scraps")
    public ResponseEntity<ScrapCreateResponseDto> create(@RequestBody ScrapDto dto){
        ScrapCreateResponseDto created=scrapService.create(dto);

        return (created!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //스크랩 전체 조회
    @Operation(summary = "스크랩 전체 조회")
    @GetMapping("/scraps")
    public ResponseEntity<List<ScrapListDto>> index(){
        List<ScrapListDto> indexed=scrapService.index();

        return (indexed!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(indexed) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //스크랩 상세보기
    @Operation(summary = "스크랩 상세보기")
    @GetMapping("/scraps/{scrapId}")
    public ResponseEntity<ScrapShowResponseDto> show(@PathVariable("scrapId") Integer scrapId){
        ScrapShowResponseDto showed= scrapService.show(scrapId);

        return (showed!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(showed) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //스크랩 수정
    @Operation(summary = "스크랩 수정")
    @PatchMapping("/scraps/{scrapId}")
    public ResponseEntity<ScrapChangeResponseDto> update(@PathVariable("scrapId") Integer scrapId, @RequestParam Integer userId, @RequestBody ScrapDto dto){
        ScrapChangeResponseDto updated=scrapService.update(scrapId, userId, dto);

        return (updated!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //스크랩 삭제
    @Operation(summary = "스크랩 삭제")
    @DeleteMapping("/scraps/{scrapId}")
    public ResponseEntity<ScrapDto> delete(@PathVariable("scrapId") Integer scrapId, @RequestParam Integer userId){
        ScrapDto deleted=scrapService.delete(scrapId,userId);
        return (deleted!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(deleted) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //즐겨찾기 토글
    @Operation(summary = "스크랩 즐겨찾기 토글")
    @PatchMapping("/scraps/{scrapId}/favorite")
    public ResponseEntity<ScrapFavoriteDto> favorite(@PathVariable("scrapId") Integer scrapId, @RequestBody ScrapFavoriteDto dto){
        ScrapFavoriteDto favorited=scrapService.favorite(scrapId, dto);
        return (favorited!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(favorited) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    //키워드 검색
    @Operation(summary = "스크랩 키워드 검색")
    @GetMapping("/scraps/search") //scraps/search?query={keyword}로 호출됨
    public ResponseEntity<List<ScrapListDto>> search(@RequestParam("keyword") String keyword){
        List<ScrapListDto> searched=scrapService.search(keyword);
        return (searched!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(searched) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
