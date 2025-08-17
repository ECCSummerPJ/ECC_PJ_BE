package com.linkrap.BE.repository;



import com.linkrap.BE.dto.FriendResponseDto;
import com.linkrap.BE.entity.Friend;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    //사용자의 모든 친구 조회
    @Query(value = "SELECT * FROM friend WHERE user_id=:userId", nativeQuery = true)
    List<Friend> findByUserId(Integer userId);
    //특정 친구 관계 조회
    @Query(value = "SELECT * FROM friend WHERE friendship_id=:friendshipId", nativeQuery = true)
    Friend findByFriendshipId(Integer friendshipId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("delete from Friend f where f.userId = :uid or f.friendUser.userId = :uid")
    int deleteAllByUserInvolved(@Param("uid") Integer uid);
}