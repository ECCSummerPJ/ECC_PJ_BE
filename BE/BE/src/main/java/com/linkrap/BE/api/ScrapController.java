package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.security.CustomUserDetails;
import com.linkrap.BE.service.CommentService;
import com.linkrap.BE.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "스크랩 API")
@SecurityRequirement(name="bearerAuth")
public class ScrapController {

    @Autowired
    private ScrapService scrapService;

    @Autowired private CommentService commentService;

    //스크랩 생성
    @Operation(summary = "스크랩 생성")
    @PostMapping("/scraps")
    public ResponseEntity<ScrapCreateResponseDto> create(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ScrapCreateRequestDto dto){
        Integer userId = userDetails.getUserId();
        ScrapCreateResponseDto created=scrapService.create(userId, dto);

        return (created!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //스크랩 전체 조회: 즐겨찾기만 보기/공개 스크랩만 보기 필터 포함
    @Operation(summary = "스크랩 전체 조회")
    @GetMapping("/scraps")
    public ResponseEntity<List<ScrapListDto>> index(@RequestParam(required=false) Boolean favorite,
                                                    @RequestParam(required=false) Boolean showPublic,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        Integer userId = userDetails.getUserId();
        List<ScrapListDto> indexed=scrapService.getAllScrapsByFilter(userId, favorite, showPublic);
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
    @Operation(summary = "스크랩 수정", description = "카테고리 수정 시 'categoryId'~'showPublic' 모두 작성, 카테고리 수정하지 않을 시 'categoryId' 삭제하고 'scrapTitle'~'showPublic'만 작성")
    @PatchMapping("/scraps/{scrapId}")
    public ResponseEntity<ScrapChangeResponseDto> update(@PathVariable("scrapId") Integer scrapId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ScrapChangeRequestDto dto){
        Integer userId = userDetails.getUserId();
        ScrapChangeResponseDto updated=scrapService.update(scrapId, userId, dto);

        return (updated!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(updated) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //스크랩 삭제
    @Operation(summary = "스크랩 삭제")
    @DeleteMapping("/scraps/{scrapId}")
    public ResponseEntity<ScrapDto> delete(@PathVariable("scrapId") Integer scrapId, @AuthenticationPrincipal CustomUserDetails userDetails){
        Integer userId = userDetails.getUserId();
        ScrapDto deleted=scrapService.delete(scrapId,userId);
        return (deleted!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(deleted) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //즐겨찾기 토글
    @Operation(summary = "스크랩 즐겨찾기 토글", description = "현재 상태가 true이면 false로, false면 true로 바뀜")
    @PatchMapping("/scraps/{scrapId}/favorite")
    public ResponseEntity<ScrapFavoriteDto> favorite(@PathVariable("scrapId") Integer scrapId){
        ScrapFavoriteDto favorited=scrapService.favorite(scrapId);
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

    //댓글 생성
    @Operation(summary = "댓글 생성")
    @PostMapping("scraps/{scrapId}/comments")
    public ResponseEntity<CommentShowDto> create(@PathVariable("scrapId") Integer scrapId, @AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody CommentCreateRequestDto dto){
        Integer userId = userDetails.getUserId();
        //서비스에 위임
        CommentShowDto createdDto=commentService.create(scrapId, userId, dto);
        //결과 응답
        return (createdDto!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdDto) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    // 스크랩 댓글 조회
    @Operation(summary = "스크랩의 댓글 목록 조회")
    @GetMapping("/scraps/{scrapId}/comments")
    public ResponseEntity<List<CommentShowDto>> listComments(
            @PathVariable int scrapId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        int userId = userDetails.getUserId();
        Page<CommentShowDto> p = commentService.listByScrap(scrapId, page, size);
        return ResponseEntity.ok(p.getContent());
    }

    //스크랩 열람 여부 기록
    @Operation(summary = "스크랩 열람 여부 기록")
    @PatchMapping("/scraps/{scrapId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Integer scrapId){
        scrapService.markAsRead(scrapId);
        return ResponseEntity.ok().build();
    }

    //리마인드 알람 목록
    @Operation(summary = "리마인드 알람 목록")
    @GetMapping("/scraps/reminders")
    public ResponseEntity<List<RemindDto>> getReminderScraps(){
        Integer userId = 1; //임시 사용자
        List<RemindDto> reminderScraps = scrapService.getUnreadScrapsByOldest(userId, 5);
        if (reminderScraps.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reminderScraps);
    }

}
