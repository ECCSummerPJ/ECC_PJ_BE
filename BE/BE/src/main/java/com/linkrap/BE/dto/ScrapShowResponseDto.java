package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ScrapShowResponseDto {
    private Integer scrapId;
    private Integer userId;
    private Integer categoryId;
    private String scrapTitle;
    private String scrapLink;
    private String scrapMemo;
    private boolean favorite;
    private boolean showPublic;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<CommentShowDto> comments;
}
