package com.linkrap.BE.api;

import com.linkrap.BE.dto.CommentCreateRequestDto;
import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.ResponseFormat;
import com.linkrap.BE.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/scraps") //localhost:8080/api/scraps 이하 요청 처리하는 컨트롤러
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

    //댓글 생성
    @Operation(summary = "댓글 생성")
    @PostMapping("/{scrapId}/comments")
    public ResponseFormat<CommentDto> create(@PathVariable("scrapId") Integer scrapId, @RequestParam Integer userId, @RequestBody CommentDto dto){
        //서비스에 위임
        CommentDto createdDto=commentService.create(scrapId, userId, dto);
        //결과 응답
        return (createdDto!=null) ?
                ResponseFormat.ok("댓글 생성 성공",createdDto) :
                ResponseFormat.notFound("스크랩을 찾을 수 없습니다.");
    }

}
