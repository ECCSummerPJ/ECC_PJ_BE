package com.linkrap.BE.api;

import com.linkrap.BE.dto.FriendRequestDto;
import com.linkrap.BE.dto.FriendResponseDto;
import com.linkrap.BE.dto.ScrapListDto;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.security.CustomUserDetails;
import com.linkrap.BE.service.FriendService;
import com.linkrap.BE.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "친구 API")
@SecurityRequirement(name="bearerAuth")
public class FriendApiController {
    @Autowired
    FriendService friendService;
    @Autowired
    ScrapService scrapService;

    //친구 조회
    @Operation(summary = "친구 목록 조회", description = "로그인한 사용자가 추가한 친구 목록 반환")
    @GetMapping("/friends")
    public ResponseEntity<List<FriendResponseDto>> friends(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer currentUserId = userDetails.getUserId();
        List<FriendResponseDto> dtos = friendService.friends(currentUserId);
        return (dtos != null) ?
                ResponseEntity.status(HttpStatus.OK).body(dtos) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //친구 등록
    @Operation(summary = "친구 등록", description = "입력 받은 ID의 사용자를, 로그인한 사용자의 친구로 추가")
    @PostMapping("/friends")
    public ResponseEntity<FriendResponseDto> create(@RequestBody FriendRequestDto dto,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer currentUserId = userDetails.getUserId();
        FriendResponseDto createdDto = friendService.create(currentUserId, dto);
        return (createdDto != null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdDto) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //친구 해제
    @Operation(summary = "친구 해제", description = "로그인한 사용자가 추가한 친구 관계를 삭제")
    @DeleteMapping("/friends/{friendshipId}")
    public ResponseEntity<FriendResponseDto> delete(@PathVariable Integer friendshipId,
                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer currentUserId = userDetails.getUserId();
        FriendResponseDto deletedDto = friendService.delete(currentUserId, friendshipId);
        return (deletedDto != null) ?
                ResponseEntity.status(HttpStatus.OK).body(deletedDto) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //친구 스크랩 목록 조회
    @Operation(summary = "친구 스크랩 목록 조회", description = "로그인한 사용자가 친구로 추가한 사용자의 스크랩 목록을 카테고리별로 조회")
    @GetMapping("/friend/{friendUserId}/scraps")
    public ResponseEntity<List<ScrapListDto>> getFriendScraps(@PathVariable Integer friendUserId,
                                                              @RequestParam(required = false) Boolean favorite,
                                                              @RequestParam(required = false) Integer categoryId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer currentUserId = userDetails.getUserId();

        // 1) 자기 자신: 내 스크랩(전체) 반환
        if (currentUserId.equals(friendUserId)) {
            List<ScrapListDto> mine = scrapService.getPublicScraps(currentUserId, favorite, categoryId);
            return ResponseEntity.ok(mine);
        }

        //친구 관계 확인
        boolean isFriend = friendService.checkFriendship(currentUserId, friendUserId);
        List<ScrapListDto> publics = scrapService.getPublicScraps(friendUserId, favorite, categoryId);
        if (isFriend) {
            // 친구라면 공개글만/혹은 정책에 따라 friend-visible 로직 사용
            return ResponseEntity.ok(publics);
        } else {
            if (publics != null && !publics.isEmpty()) {
                return ResponseEntity.ok(publics);  // 비친구도 공개글은 OK
            }
            // 공개글도 없으면 금지
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
