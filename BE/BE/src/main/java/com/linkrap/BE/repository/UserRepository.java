package com.linkrap.BE.repository;

import com.linkrap.BE.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    Optional<Users> findByUserId(String userId);
}