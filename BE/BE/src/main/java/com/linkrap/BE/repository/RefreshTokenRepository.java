package com.linkrap.BE.repository;

import com.linkrap.BE.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    void deleteByToken(String token);

    @Modifying
    @Query("delete from RefreshToken t where t.user.userId = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);
}
