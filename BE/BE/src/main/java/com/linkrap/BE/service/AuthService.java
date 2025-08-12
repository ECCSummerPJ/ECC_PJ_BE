package com.linkrap.BE.auth.service;

import com.linkrap.be.auth.domain.User;
import com.linkrap.be.auth.dto.AuthResponse;
import com.linkrap.be.auth.dto.JoinForm;
import com.linkrap.be.auth.dto.LoginRequest;
import com.linkrap.be.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public AuthResponse join(JoinForm form) {

        if (userRepository.existsByUserId(form.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }


        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("비밀번호와 새 비밀번호가 일치하지 않습니다.");
        }


        User u = new User();
        u.setUserId(form.getUserId().trim());
        u.setEmail(form.getEmail().trim());
        u.setNickname(form.getNickname().trim());
        u.setPasswordHash(passwordEncoder.encode(form.getPassword())); // 해시 저장
        u.setProfileImageUrl(form.getProfileImageUrl());

        User saved  = userRepository.save(u);

        return new AuthResponse(
                saved.getId(),
                saved.getUserId(),
                saved.getEmail(),
                saved.getNickname(),
                saved.getProfileImageUrl()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        User user = userRepository.findByUserId(req.userId().trim())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return new AuthResponse(
                user.getId(),
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}
