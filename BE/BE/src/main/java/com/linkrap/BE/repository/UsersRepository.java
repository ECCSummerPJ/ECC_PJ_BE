package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Integer> {
    // ID, 이메일 중복 체크용
    boolean existsByUserId(String loginId);
    boolean existsByEmail(String email);
    Optional<Users> findByUserId(String loginId);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);
}
