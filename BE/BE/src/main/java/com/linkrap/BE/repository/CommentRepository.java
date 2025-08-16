package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Comment;
import com.linkrap.BE.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Modifying
    //scrap 지워지면 해당 comment들도 지워지도록 하는 기능
    @Query("""
        delete
        from Comment c
        where c.scrap.scrapId=:scrapId
    """)
    void deleteComment(@Param("scrapId") Integer scrapId);

}
