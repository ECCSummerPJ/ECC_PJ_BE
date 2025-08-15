package com.linkrap.BE.api;

import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentUpdateRequestDto;
import com.linkrap.BE.dto.ResponseFormat;
import com.linkrap.BE.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "comment-controller")
public class CommentApiController {

    @Autowired CommentService commentService;

    // 댓글 수정
    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(
            @PathVariable int commentId,
            @RequestParam int userId, // 임시: 인증 붙기 전
            @RequestBody CommentUpdateRequestDto dto
    ) {
        try {
            CommentDto d = commentService.update(commentId, userId, dto);
            return ResponseEntity.status(HttpStatus.OK).body(d);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable int commentId,
            @RequestParam int userId // 임시
    ) {
        try {
            commentService.delete(commentId, userId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
