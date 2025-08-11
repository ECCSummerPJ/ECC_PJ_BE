package com.linkrap.BE.comment.controller;

import com.linkrap.BE.comment.dto.CommentResponseDto;
import com.linkrap.BE.comment.dto.CommentUpdateRequestDto;
import com.linkrap.BE.comment.service.CommentService;
import com.linkrap.BE.profile.dto.ResponseFormat;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API", description = "댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    // 댓글 수정
    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseFormat<CommentResponseDto> update(
            @PathVariable Long commentId,
            @RequestParam Long userId, // 임시: 인증 붙기 전
            @RequestBody CommentUpdateRequestDto req
    ) {
        try {
            CommentResponseDto dto = commentService.update(commentId, userId, req);
            return ResponseFormat.ok("댓글 수정 완료", dto);
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
            @PathVariable Long commentId,
            @RequestParam Long userId // 임시
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
