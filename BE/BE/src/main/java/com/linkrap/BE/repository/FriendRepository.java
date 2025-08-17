package com.linkrap.BE.repository;



import com.linkrap.BE.dto.FriendResponseDto;
import com.linkrap.BE.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    //사용자의 모든 친구 조회
    @Query(value = "SELECT * FROM friend WHERE user_id=:userId", nativeQuery = true)
    List<Friend> findByUserId(Integer userId);
    //특정 친구 관계 조회
    @Query(value = "SELECT * FROM friend WHERE friendship_id=:friendshipId", nativeQuery = true)
    Friend findByFriendshipId(Integer friendshipId);
    //친구 관계 확인
    @Query(value = "SELECT CASE WHEN COUNT(f.user_id) > 0 THEN true ELSE false END " +
            "FROM friend f " +
            "INNER JOIN users u ON f.friend_user_id = u.user_id " +
            "WHERE f.user_id = :userId AND u.user_id = :friendUserId", nativeQuery = true)
    boolean existsByUserIdAndFriendUserId(Integer userId, Integer friendUserId);
}