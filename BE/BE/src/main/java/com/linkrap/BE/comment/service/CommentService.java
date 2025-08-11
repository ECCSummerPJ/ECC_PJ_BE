package com.linkrap.BE.comment.service;


import com.linkrap.BE.comment.dto.CommentResponseDto;
import com.linkrap.BE.comment.dto.CommentUpdateRequestDto;
import com.linkrap.BE.comment.entity.Comment;
import com.linkrap.BE.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 조회
    @Transactional(readOnly = true)
    public CommentResponseDto get(Long commentId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        return new CommentResponseDto(
                c.getCommentsId(), c.getScrap().getScrapId(), c.getAuthor().getUserId(), c.getContent()
        );
    }

    // 댓글 수정
    public CommentResponseDto update(Long commentId, Long requestUserId, CommentUpdateRequestDto req) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // (인증 붙으면) 권한 체크: 본인만 수정
        if (!c.getAuthor().getUserId().equals(requestUserId)) {
            throw new IllegalArgumentException("본인 댓글만 수정할 수 있습니다.");
        }
        if (req.getContent() == null || req.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (req.getContent().length() > 300) {
            throw new IllegalArgumentException("내용은 300자 이하여야 합니다.");
        }

        c.setContent(req.getContent().trim());
        commentRepository.save(c);

        return new CommentResponseDto(
                c.getCommentsId(), c.getScrap().getScrapId(), c.getAuthor().getUserId(), c.getContent()
        );
    }

    // 댓글 삭제
    public void delete(Long commentId, Long requestUserId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!c.getAuthor().getUserId().equals(requestUserId)) {
            throw new IllegalArgumentException("본인 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(c);
    }
}

