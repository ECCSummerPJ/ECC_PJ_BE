package com.linkrap.BE.api;

import com.linkrap.BE.dto.FriendRequestDto;
import com.linkrap.BE.dto.FriendResponseDto;
import com.linkrap.BE.dto.ResponseFormat;
import com.linkrap.BE.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendApiController {
    @Autowired
    FriendService friendService;
    //친구 조회
    @GetMapping("/api/friends")
    public ResponseFormat<List<FriendResponseDto>> friends(){
        List<FriendResponseDto> dtos = friendService.friends();
        return (dtos!=null) ?
                ResponseFormat.ok("친구 목록 조회 완료", dtos):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //친구 등록
    @PostMapping("/api/friends")
    public ResponseFormat<FriendResponseDto> create(@RequestBody FriendRequestDto dto){
        Integer userId = 1; //임시 사용자
        FriendResponseDto createdDto = friendService.create(userId, dto);
        return (createdDto!=null) ?
                ResponseFormat.ok("친구 추가 완료", createdDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //친구 해제
    @DeleteMapping("/api/friends/{friendshipId}")
    public ResponseFormat<FriendResponseDto> delete(@PathVariable Integer friendshipId){
        FriendResponseDto deletedDto = friendService.delete(friendshipId);
        return (deletedDto!=null) ?
                ResponseFormat.ok("친구 삭제 완료", deletedDto):
                ResponseFormat.failure("요청 형식이 올바르지 않습니다.");
    }
    //친구 스크랩 목록
}
