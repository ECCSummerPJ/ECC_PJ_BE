package com.linkrap.BE.entity;

import com.linkrap.BE.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users author;

    @ManyToOne
    @JoinColumn(name="scrap_id")
    private Scrap scrap;

    @Column
    private String content;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Comment createComment(CommentDto dto, Scrap scrap, Users user) {
        //예외 발생
        if (dto.getCommentId() != null)
            throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
        //if (dto.getScrapId()!=scrap.getScrapId())
        //    throw new IllegalArgumentException("댓글 생성 실패! 게시글의 id가 잘못됐습니다.");
        //엔티티 생성 및 반환
        return new Comment(
                dto.getCommentId(),
                user,
                scrap,
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }

    public Integer getScrapIdValue() {
        return scrap.getScrapId();
    }
}
