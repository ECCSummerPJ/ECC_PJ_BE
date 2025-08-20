package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Users;
//<<<<<<< HEAD
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UsersRepository extends CrudRepository<Users, Integer> {

    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Users> findByLoginId(String loginId);


}
