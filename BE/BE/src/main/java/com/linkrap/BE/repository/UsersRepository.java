package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    // ID, 이메일 중복 체크용
    boolean existsByUserId(Integer loginId);
    boolean existsByEmail(String email);
    Optional<Users> findByUserId(Integer loginId);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);
}
