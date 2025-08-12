package com.linkrap.BE.auth.repository;

import com.linkrap.be.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    Optional<User> findByUserId(String userId);
}