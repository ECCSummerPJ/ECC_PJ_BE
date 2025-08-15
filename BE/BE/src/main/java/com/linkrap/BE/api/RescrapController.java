package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.Rescrap;
import com.linkrap.BE.service.RescrapService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api") //localhost:8080/api 이하 요청 처리하는 컨트롤러
@Slf4j
public class RescrapController {

    @Autowired
    private RescrapService rescrapService;


    //리스크랩 생성
    //**자기 스크랩은 리스크랩 못하게 조건 추가해야 함**
    @Operation(summary = "리스크랩 생성")
    @PostMapping("/scraps/{scrapId}/rescraps")
    public ResponseFormat<RescrapCreateResponseDto> create(@PathVariable("scrapId") Integer scrapId, @RequestBody RescrapDto dto){
        RescrapCreateResponseDto created= rescrapService.create(scrapId, dto);

        return (created!=null) ?
                ResponseFormat.created("스크랩 생성 완료",created) :
                ResponseFormat.failure("요청 형식이 올바르지 않습니다");
    }

    //리스크랩 상세보기
    @Operation(summary = "리스크랩 상세보기")
    @GetMapping("/rescraps/{rescrapId}")
    public ResponseFormat<RescrapShowResponseDto> show(@PathVariable("rescrapId") Integer rescrapId){
        RescrapShowResponseDto showed= rescrapService.show(rescrapId);

        return (showed!=null) ?
                ResponseFormat.ok("스크랩 조회 성공",showed) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

    //리스크랩 삭제
    @Operation(summary = "리스크랩 삭제")
    @DeleteMapping("/rescraps/{rescrapId}")
    public ResponseFormat<Rescrap> delete(@PathVariable("rescrapId") Integer rescrapId){
        Rescrap deleted=rescrapService.delete(rescrapId);
        return (deleted!=null) ?
                ResponseFormat.ok("스크랩 삭제 성공",null) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }
}
