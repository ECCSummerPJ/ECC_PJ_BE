package com.linkrap.BE.profile.repository;

import com.linkrap.BE.profile.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    // 이메일 중복 체크용
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    // 닉네임 중복 확인
    boolean existsByNickname(String nickname);
}
