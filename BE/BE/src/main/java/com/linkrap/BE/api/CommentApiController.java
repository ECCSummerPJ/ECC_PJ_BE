package com.linkrap.BE.api;

import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentUpdateRequestDto;
import com.linkrap.BE.dto.ResponseFormat;
import com.linkrap.BE.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "comment-controller")
public class CommentApiController {

    @Autowired CommentService commentService;

    // 댓글 수정
    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseFormat<CommentDto> update(
            @PathVariable int commentId,
            @RequestParam int userId, // 임시: 인증 붙기 전
            @RequestBody CommentUpdateRequestDto dto
    ) {
        try {
            CommentDto d = commentService.update(commentId, userId, dto);
            return ResponseFormat.ok("댓글 수정 완료", d);
        } catch (IllegalArgumentException e) {
            return ResponseFormat.failure(e.getMessage());
        } catch (Exception e) {
            return ResponseFormat.error("서버 내부 오류");
        }
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseFormat<Void> delete(
            @PathVariable int commentId,
            @RequestParam int userId // 임시
    ) {
        try {
            commentService.delete(commentId, userId);
            return ResponseFormat.ok("댓글 삭제 완료", null);
        } catch (IllegalArgumentException e) {
            return ResponseFormat.failure(e.getMessage());
        } catch (Exception e) {
            return ResponseFormat.error("서버 내부 오류");
        }
    }
}
