package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.Rescrap;
import com.linkrap.BE.service.RescrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "리스크랩 API")
public class RescrapController {

    @Autowired
    private RescrapService rescrapService;


    //리스크랩 생성
    //**자기 스크랩은 리스크랩 못하게 조건 추가해야 함**
    @Operation(summary = "리스크랩 생성")
    @PostMapping("/scraps/{scrapId}/rescraps")
    public ResponseEntity<RescrapCreateResponseDto> create(@PathVariable("scrapId") Integer scrapId, @RequestParam Integer userId, @RequestBody RescrapDto dto){
        RescrapCreateResponseDto created= rescrapService.create(scrapId, userId, dto);

        return (created!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //리스크랩 상세보기
    @Operation(summary = "리스크랩 상세보기")
    @GetMapping("/rescraps/{rescrapId}")
    public ResponseEntity<RescrapShowResponseDto> show(@PathVariable("rescrapId") Integer rescrapId){
        RescrapShowResponseDto showed= rescrapService.show(rescrapId);

        return (showed!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(showed) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //리스크랩 삭제
    @Operation(summary = "리스크랩 삭제")
    @DeleteMapping("/rescraps/{rescrapId}")
    public ResponseEntity<RescrapDto> delete(@PathVariable("rescrapId") Integer rescrapId, @RequestParam Integer userId){
        RescrapDto deleted=rescrapService.delete(rescrapId, userId);
        return (deleted!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(deleted) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
