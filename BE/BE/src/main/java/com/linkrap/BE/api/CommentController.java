package com.linkrap.BE.api;

import com.linkrap.BE.dto.CommentCreateRequestDto;
import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentShowDto;
import com.linkrap.BE.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/scraps") //localhost:8080/api/scraps 이하 요청 처리하는 컨트롤러
@Slf4j
@Tag(name = "댓글 API")
public class CommentController {
    @Autowired
    private CommentService commentService;

//    //댓글 생성
//    @Operation(summary = "댓글 생성")
//    @PostMapping("/{scrapId}/comments")
//    public ResponseEntity<CommentShowDto> create(@PathVariable("scrapId") Integer scrapId, @RequestParam Integer userId, @RequestBody CommentCreateRequestDto dto){
//        //서비스에 위임
//        CommentShowDto createdDto=commentService.create(scrapId, userId, dto);
//        //결과 응답
//        return (createdDto!=null) ?
//                ResponseEntity.status(HttpStatus.OK).body(createdDto) :
//                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }

}
