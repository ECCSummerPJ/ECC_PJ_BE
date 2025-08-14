package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.service.ScrapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") //localhost:8080/api/scraps 이하 요청 처리하는 컨트롤러
@Slf4j
public class ScrapController {

    @Autowired
    private ScrapService scrapService;

    //스크랩 생성
    @PostMapping("/scraps")
    public ResponseFormat<ScrapCreateResponseDto> create(@RequestBody ScrapDto dto){
        ScrapCreateResponseDto created=scrapService.create(dto);

        return (created!=null) ?
                ResponseFormat.created("스크랩 생성 완료",created) :
                ResponseFormat.failure("요청 형식이 올바르지 않습니다");
    }

    //스크랩 전체 조회
    @GetMapping("/scraps")
    public ResponseFormat<List<Scrap>> index(){
        List<Scrap> indexed=scrapService.index();

        return (indexed!=null) ?
                ResponseFormat.ok("스크랩 조회 성공",indexed) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //스크랩 상세보기
    @GetMapping("/scraps/{scrapId}")
    public ResponseFormat<ScrapShowResponseDto> show(@PathVariable("scrapId") Integer scrapId){
        ScrapShowResponseDto showed= scrapService.show(scrapId);

        return (showed!=null) ?
                ResponseFormat.ok("스크랩 조회 성공",showed) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //스크랩 수정
    @PatchMapping("/scraps/{scrapId}")
    public ResponseFormat<ScrapChangeResponseDto> update(@PathVariable("scrapId") Integer scrapId, @RequestBody ScrapChangeRequestDto dto){
        ScrapChangeResponseDto updated=scrapService.update(scrapId, dto);

        return (updated!=null) ?
                ResponseFormat.ok("스크랩 수정 성공",updated) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //스크랩 삭제
    @DeleteMapping("/scraps/{scrapId}")
    public ResponseFormat<Scrap> delete(@PathVariable("scrapId") Integer scrapId){
        Scrap deleted=scrapService.delete(scrapId);
        return (deleted!=null) ?
                ResponseFormat.ok("스크랩 삭제 성공",null) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //즐겨찾기 토글
    @PatchMapping("/scraps/{scrapId}/favorite")
    public ResponseFormat<ScrapFavoriteDto> favorite(@PathVariable("scrapId") Integer scrapId, @RequestBody ScrapFavoriteDto dto){
        ScrapFavoriteDto favorited=scrapService.favorite(scrapId, dto);
        return (favorited!=null) ?
                ResponseFormat.ok("스크랩 즐겨찾기 성공",favorited) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }


    //키워드 검색
    @GetMapping("/scraps/search?query={keyword}")
    public ResponseFormat<List<ScrapDto>> search(@PathVariable("keyword") String keyword){
        List<ScrapDto> searched=scrapService.search(keyword);
        return (searched!=null) ?
                ResponseFormat.ok("키워드 검색 성공",searched) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }


}
