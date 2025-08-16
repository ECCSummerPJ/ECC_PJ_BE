package com.linkrap.BE.api;

import com.linkrap.BE.dto.FriendRequestDto;
import com.linkrap.BE.dto.FriendResponseDto;
import com.linkrap.BE.dto.ResponseFormat;
import com.linkrap.BE.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "친구 API")
public class FriendApiController {
    @Autowired
    FriendService friendService;

    //친구 조회
    @Operation(summary = "친구 목록 조회", description = "로그인한 사용자가 추가한 친구 목록 반환")
    @GetMapping("/api/friends")
    public ResponseEntity<List<FriendResponseDto>> friends(){
        Integer userId = 1; //임시 사용자
        List<FriendResponseDto> dtos = friendService.friends(userId);
        return (dtos!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(dtos) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    //친구 등록
    @Operation(summary = "친구 등록", description = "입력 받은 ID의 사용자를, 로그인한 사용자의 친구로 추가")
    @PostMapping("/api/friends")
    public ResponseEntity<FriendResponseDto> create(@RequestBody FriendRequestDto dto){
        Integer userId = 1; //임시 사용자
        FriendResponseDto createdDto = friendService.create(userId, dto);
        return (createdDto!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(createdDto) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //친구 해제
    @Operation(summary = "친구 해제", description = "로그인한 사용자가 추가한 친구 관계를 삭제")
    @DeleteMapping("/api/friends/{friendshipId}")
    public ResponseEntity<FriendResponseDto> delete(@PathVariable Integer friendshipId){
        FriendResponseDto deletedDto = friendService.delete(friendshipId);
        return (deletedDto!=null) ?
                ResponseEntity.status(HttpStatus.OK).body(deletedDto) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    //친구 스크랩 목록
    //@GetMapping("/api/friend/{friendUserId}/scraps")


}
