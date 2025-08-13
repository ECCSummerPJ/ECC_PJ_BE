package com.linkrap.BE.service;

import com.linkrap.BE.domain.User;
import com.linkrap.BE.dto.AuthResponse;
import com.linkrap.BE.dto.JoinForm;
import com.linkrap.BE.dto.LoginRequest;
import com.linkrap.BE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse join(JoinForm form) {

        String userId   = ntrim(form.getUserId());
        String email    = lower(ntrim(form.getEmail()));
        String nickname = ntrim(form.getNickname());
        String password = form.getPassword();


        if (isBlank(userId))   throw new IllegalArgumentException("아이디를 입력해주세요.");
        if (isBlank(email))    throw new IllegalArgumentException("이메일을 입력해주세요.");
        if (isBlank(password)) throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        if (isBlank(nickname)) throw new IllegalArgumentException("닉네임을 입력해주세요.");

        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        
        if (userRepository.existsByUserId(form.getUserId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }



        User u = new User();
        u.setUserId(userId);
        u.setEmail(email);
        u.setNickname(nickname);
        u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        u.setProfileImageUrl(ntrim(form.getProfileImageUrl()));


        try {
            User saved = userRepository.save(u);
            return new AuthResponse(
                    saved.getId(),
                    saved.getUserId(),
                    saved.getEmail(),
                    saved.getNickname(),
                    saved.getProfileImageUrl()
            );
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 사용 중인 아이디 또는 이메일입니다.");
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest req) {
        String userId = ntrim(req.userId());
        String raw    = req.password();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(raw, user.getPasswordHash())) {
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
    private static String ntrim(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
    private static String lower(String s) {
        return s == null ? null : s.toLowerCase(Locale.ROOT);
    }
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
