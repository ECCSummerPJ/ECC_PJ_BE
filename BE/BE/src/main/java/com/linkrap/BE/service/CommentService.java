package com.linkrap.BE.service;


import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentUpdateRequestDto;
import com.linkrap.BE.entity.Comment;
import com.linkrap.BE.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    // 댓글 조회
    @Transactional(readOnly = true)
    public CommentDto get(int commentId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        return CommentDto.createCommentDto(c);
    }

    // 댓글 수정
    public CommentDto update(int commentId, int requestUserId, CommentUpdateRequestDto dto) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getContent().length() > 300) {
            throw new IllegalArgumentException("내용은 300자 이하여야 합니다.");
        }

        c.setContent(dto.getContent().trim());
        Comment updated = commentRepository.save(c);

        return CommentDto.createCommentDto(updated);
    }


    // 댓글 삭제
    public void delete(int commentId, int requestUserId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!c.getAuthorId().getUserId().equals(requestUserId)) {
            throw new IllegalArgumentException("본인 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(c);
    }
}

