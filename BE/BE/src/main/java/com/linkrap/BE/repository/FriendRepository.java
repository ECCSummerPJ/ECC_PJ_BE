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



    //탈퇴 응답이 500->아래 코드 추가함으로써
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("delete from Friend f where f.userId = :uid or f.friendUser.userId = :uid")
    int deleteAllByUserInvolved(@Param("uid") Integer uid);



    @Query(value = "SELECT CASE WHEN COUNT(f.user_id) > 0 THEN true ELSE false END " +
            "FROM friend f " +
            "INNER JOIN users u ON f.friend_user_id = u.user_id " +
            "WHERE f.user_id = :userId AND u.user_id = :friendUserId", nativeQuery = true)
    boolean existsByUserIdAndFriendUserId(Integer userId, Integer friendUserId);

}