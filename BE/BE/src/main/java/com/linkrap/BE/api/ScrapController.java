package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.service.ScrapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scraps") //localhost:8080/api/ 이하 요청 처리하는 컨트롤러
@Slf4j
public class ScrapController {

    @Autowired
    private final ScrapService scrapService;

    public ScrapController(ScrapService scrapService) {
        this.scrapService = scrapService;
    }

    //스크랩 생성
    @PostMapping("")
    public ResponseFormat<ScrapCreateResponseDto> create(@RequestBody ScrapCreateRequestDto dto){
        ScrapCreateResponseDto created=scrapService.create(dto);

        return (created!=null) ?
                ResponseFormat.created("스크랩 생성 완료",created) :
                ResponseFormat.failure("요청 형식이 올바르지 않습니다");
    }

    //스크랩 상세보기
    @GetMapping("/{scrapId}")
    public ResponseFormat<Scrap> show(@PathVariable Integer scrapId){
        Scrap showed= scrapService.show(scrapId);

        return (showed!=null) ?
                ResponseFormat.ok("스크랩 조회 성공",showed) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //스크랩 수정
    @PatchMapping("/{scrapId}")
    public ResponseFormat<ScrapChangeResponseDto> update(@PathVariable Integer scrapId, @RequestBody ScrapChangeRequestDto dto){
        ScrapChangeResponseDto updated=scrapService.update(scrapId, dto);

        return (updated!=null) ?
                ResponseFormat.ok("스크랩 수정 성공",updated) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //스크랩 삭제
    @DeleteMapping("/{scrapId}")
    public ResponseFormat<Scrap> delete(@PathVariable Integer scrapId){
        Scrap deleted=scrapService.delete(scrapId);
        return (deleted!=null) ?
                ResponseFormat.ok("스크랩 수정 성공",null) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //즐겨찾기 토글
    @PatchMapping("/{scrapId}/favorite")
    public ResponseFormat<ScrapFavoriteDto> favorite(@PathVariable Integer scrapId,@RequestBody ScrapFavoriteDto dto){
        ScrapFavoriteDto favorited=scrapService.favorite(scrapId, dto);
        return (favorited!=null) ?
                ResponseFormat.ok("스크랩 삭제 성공",favorited) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //리스크랩
    /*
    @PostMapping("{scrapId}/rescraps")
    public ResponseFormat<ReScrap> rescrap(@PathVariable Integer scrapId, @RequestBody RescrapRequestDto dto){

    }
     */
}
