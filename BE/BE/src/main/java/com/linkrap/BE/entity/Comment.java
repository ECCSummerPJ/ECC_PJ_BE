package com.linkrap.BE.entity;

import com.linkrap.BE.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Getter
@Builder
public class Comment {
    @Id //PK
    @GeneratedValue(strategy= GenerationType.IDENTITY) //숫자 자동으로 매겨짐
    @Column(name="comment_id")
    private Integer commentId;

    @ManyToOne //FK
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne //FK
    @JoinColumn(name="scrap_id") //원본 스크랩
    private Scrap scrap;

    @Column(name="content")
    private String commentContent;

    @Column(name="created_at")
    @CreationTimestamp
    private Timestamp createdAt;


    public static Comment createComment(CommentDto dto, Scrap scrap, Users user) {
        //예외 발생
        if (dto.getCommentId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        if (!Objects.equals(dto.getScrapId(), scrap.getScrapId()))
            throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못됐습니다.");
        //엔티티 생성 및 반환
        return new Comment(
                dto.getCommentId(),
                user,
                scrap,
                dto.getCommentContent(),
                dto.getCreatedAt()
        );
    }

}
