package com.linkrap.BE.dto;

import com.linkrap.BE.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor //모든 필드를 매개변수로 갖는 생성자 자동 생성
@NoArgsConstructor //매개변수가 아예 없는 기본 생성자 자동 생성
@Getter //각 필드 값을 조회할 수 있는 getter 메서드 자동 생성
@ToString //모든 필드를 출력할 수 있는 toString 메서드 자동 생성
public class CommentShowDto {
    private Integer commentId;
    private Integer scrapId;
    private Integer authorId;
    private String  authorNickname;
    private String  content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isMine;  // ← Boolean 권장

    public static CommentShowDto of(Comment c, Integer requestUserId) {
        boolean mine = requestUserId != null
                && c.getAuthor() != null
                && requestUserId.equals(c.getAuthor().getUserId());
        return new CommentShowDto(
                c.getCommentId(),
                c.getScrap().getScrapId(),
                c.getAuthor().getUserId(),
                c.getAuthor().getNickname(),
                c.getContent(),
                c.getCreatedAt(),
                c.getUpdatedAt(),
                mine
        );
    }
}
