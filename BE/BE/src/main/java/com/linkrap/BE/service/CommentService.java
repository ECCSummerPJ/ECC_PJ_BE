package com.linkrap.BE.service;

import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentShowDto;
import com.linkrap.BE.dto.CommentUpdateRequestDto;
import com.linkrap.BE.entity.Comment;
import com.linkrap.BE.entity.Scrap;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.CommentRepository;
import com.linkrap.BE.repository.ScrapRepository;
import com.linkrap.BE.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private final ScrapRepository scrapRepository;
    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final UsersRepository usersRepository;

    @Transactional
    public CommentShowDto create(Integer scrapId, Integer requestUserId, CommentDto dto) {
        //1. 게시글 조회 및 예외 발생
        Scrap scrap=scrapRepository.findById(scrapId)
                .orElseThrow(()->new IllegalArgumentException("댓글 생성 실패! "+"대상 게시글이 없습니다."));
        //2. 댓글 엔티티 생성
        Users user= usersRepository.findById(requestUserId)
                .orElseThrow(()->new IllegalArgumentException("댓글 생성 실패! "+"대상 생성자가 없습니다."));

        //댓글 작성 제한
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("내용을 입력하세요.");
        }
        if (dto.getContent().length() > 300) {
            throw new IllegalArgumentException("내용은 300자 이하여야 합니다.");
        }
        Comment comment=Comment.createComment(dto,scrap,user);
        //3. 댓글 엔티티를 DB에 저장
        Comment created=commentRepository.save(comment);
        //4. DTO로 변환해 반환
        return CommentShowDto.createCommentShowDto(created);
    }

    // 댓글 조회
    @Transactional(readOnly = true)
    public CommentShowDto get(int commentId) {
        Comment c = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        return CommentShowDto.createCommentShowDto(c);
    }

    // 댓글 수정
    public CommentDto update(int commentId, int requestUserId, CommentUpdateRequestDto dto) {
        Comment c = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!c.getAuthor().getUserId().equals(requestUserId)) {
            throw new IllegalArgumentException("본인 댓글만 수정할 수 있습니다.");
        }

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
        if (!c.getAuthor().getUserId().equals(requestUserId)) {
            throw new IllegalArgumentException("본인 댓글만 삭제할 수 있습니다.");
        }
        commentRepository.delete(c);
    }
}
