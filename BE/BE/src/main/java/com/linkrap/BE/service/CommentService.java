package com.linkrap.BE.service;

import com.linkrap.BE.dto.CommentDto;
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
    public CommentDto create(Integer scrapId, CommentDto dto) {
        //1. 게시글 조회 및 예외 발생
        Scrap scrap=scrapRepository.findById(scrapId)
                .orElseThrow(()->new IllegalArgumentException("댓글 생성 실패! "+"대상 게시글이 없습니다."));
        //2. 댓글 엔티티 생성
        Users user= usersRepository.findById(dto.getUserId())
                .orElseThrow(()->new IllegalArgumentException("댓글 생성 실패! "+"대상 생성자가 없습니다."));
        Comment comment=Comment.createComment(dto,scrap,user);
        //3. 댓글 엔티티를 DB에 저장
        Comment created=commentRepository.save(comment);
        //4. DTO로 변환해 반환
        return CommentDto.createCommentDto(created);
    }

}
