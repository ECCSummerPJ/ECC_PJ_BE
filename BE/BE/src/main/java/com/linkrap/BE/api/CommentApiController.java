package com.linkrap.BE.api;

import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentUpdateRequestDto;
import com.linkrap.BE.security.CustomUserDetails;
import com.linkrap.BE.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "댓글 API")
@SecurityRequirement(name="bearerAuth")
public class CommentApiController {

    @Autowired CommentService commentService;

    // 댓글 수정
    @Operation(summary = "댓글 수정")
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(
            @PathVariable int commentId,
            @RequestBody CommentUpdateRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Integer userId = userDetails.getUserId();
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
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        try {
            commentService.delete(commentId, user.getUserId());
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
