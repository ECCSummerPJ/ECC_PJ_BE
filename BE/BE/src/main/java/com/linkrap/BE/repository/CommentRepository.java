package com.linkrap.BE.repository;

import com.linkrap.BE.dto.CommentDto;
import com.linkrap.BE.dto.CommentShowDto;
import com.linkrap.BE.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 목록 (isMine 계산 포함)
    @Query("""
        select new com.linkrap.BE.dto.CommentShowDto(
            c.commentId, c.scrap.scrapId, c.author.userId, c.author.nickname,
            c.content, c.createdAt, c.updatedAt,
            case when (:currentUserId is not null and c.author.userId = :currentUserId)
                 then true else false end
        )
        from Comment c
        where c.scrap.scrapId = :scrapId
        order by c.createdAt desc, c.commentId desc
    """)
    List<CommentShowDto> findCommentsByScrapId(@Param("scrapId") Integer scrapId,
                                               @Param("currentUserId") Integer currentUserId);

    // (선택) 기존 호출부 호환용 오버로드
    default List<CommentShowDto> findCommentsByScrapId(Integer scrapId) {
        return findCommentsByScrapId(scrapId, null);
    }

    // 페이지
    @Query(value = """
        select new com.linkrap.BE.dto.CommentShowDto(
            c.commentId, c.scrap.scrapId, c.author.userId, c.author.nickname,
            c.content, c.createdAt, c.updatedAt,
            case when (:currentUserId is not null and c.author.userId = :currentUserId)
                 then true else false end
        )
        from Comment c
        where c.scrap.scrapId = :scrapId
        order by c.createdAt desc, c.commentId desc
        """,
            countQuery = """
        select count(c)
        from Comment c
        where c.scrap.scrapId = :scrapId
        """)
    Page<CommentShowDto> findPageByScrapId(@Param("scrapId") Integer scrapId,
                                           @Param("currentUserId") Integer currentUserId,
                                           Pageable pageable);
}