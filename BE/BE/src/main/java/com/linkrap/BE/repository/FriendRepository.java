package com.linkrap.BE.repository;

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
    Friend findByFriendshipId(Integer friendshipId);
}
