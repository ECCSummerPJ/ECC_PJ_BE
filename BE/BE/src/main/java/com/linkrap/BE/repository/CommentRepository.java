package com.linkrap.BE.repository;

import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentShowDto;
import com.linkrap.BE.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Modifying
    //scrap 지워지면 해당 comment들도 지워지도록 하는 기능
    @Query("""
        delete
        from Comment c
        where c.scrap.scrapId=:scrapId
    """)
    void deleteComment(@Param("scrapId") Integer scrapId);

    @Query("""
        select new com.linkrap.BE.dto.CommentShowDto(
            c.commentId, c.scrap.scrapId, c.author.userId, c.author.nickname, c.content, c.createdAt, c.updatedAt
        )
        from Comment c
        where c.scrap.scrapId=:scrapId
    """)
    List<CommentShowDto> findCommentsByScrapId(@Param("scrapId") Integer scrapId);

}
