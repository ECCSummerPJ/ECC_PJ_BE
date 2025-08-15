//package com.linkrap.BE.service;
//
//import com.linkrap.BE.dto.AuthResponse;
//import com.linkrap.BE.dto.JoinForm;
//import com.linkrap.BE.dto.LoginRequest;
//import com.linkrap.BE.entity.Users;
//import com.linkrap.BE.repository.UsersRepository;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UsersRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Transactional
//    public AuthResponse join(JoinForm form) {
//
//        if (userRepository.existsByLoginId(form.getUserId())) {
//            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
//        }
//        if (userRepository.existsByEmail(form.getEmail())) {
//            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
//        }
//
//
//        if (!form.passwordsMatch()) {
//            throw new IllegalArgumentException("비밀번호와 새 비밀번호가 일치하지 않습니다.");
//        }
//
//
//        Users u = new Users();
//        u.setLoginId(form.getUserId().trim());
//        u.setEmail(form.getEmail().trim());
//        u.setNickname(form.getNickname().trim());
//        u.setPasswordHash(passwordEncoder.encode(form.getPassword())); // 해시 저장
//        u.setProfileImage(form.getProfileImageUrl());
//
//        Users saved  = userRepository.save(u);
//
//        return new AuthResponse(
//                saved.getUserId(),
//                saved.getLoginId(),
//                saved.getEmail(),
//                saved.getNickname(),
//                saved.getProfileImage()
//        );
//    }
//
//    @Transactional(readOnly = true)
//    public AuthResponse login(LoginRequest req) {
//        Users user = userRepository.findByLoginId(req.userId().trim())
//                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));
//
//        if (!passwordEncoder.matches(req.password(), user.getPasswordHash())) {
//            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
//        }
//
//        return new AuthResponse(
//                user.getUserId(),
//                user.getLoginId(),
//                user.getEmail(),
//                user.getNickname(),
//                user.getProfileImage()
//        );
//    }
//}
