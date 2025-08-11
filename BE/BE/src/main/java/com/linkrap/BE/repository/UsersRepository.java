package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Integer> {
    // 이메일 중복 체크용
    boolean existsByEmail(String email);
    Optional<Users> findByEmail(String email);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);
}
