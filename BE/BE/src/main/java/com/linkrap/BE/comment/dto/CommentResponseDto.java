package com.linkrap.BE.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private Long scrapId;
    private Long authorId;
    private String content;
}