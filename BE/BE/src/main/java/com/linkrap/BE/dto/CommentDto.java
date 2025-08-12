package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class CommentDto {
    private Integer commentId;
    private Integer scrapId;
    private Integer authorId;
    private String content;

    public static CommentDto createCommentDto(Comment comment) {
        return new CommentDto(
            comment.getCommentId(), comment.getScrapId().getScrapId(), comment.getAuthorId().getUserId(), comment.getContent()
        );
    }
}