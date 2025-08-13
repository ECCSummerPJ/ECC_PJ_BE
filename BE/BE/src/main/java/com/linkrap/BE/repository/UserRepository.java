package com.linkrap.BE.repository;

import com.linkrap.BE.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    Optional<User> findByUserId(String userId);
}