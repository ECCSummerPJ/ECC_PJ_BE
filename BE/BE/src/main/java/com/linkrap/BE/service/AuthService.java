package com.linkrap.BE.service;


import com.linkrap.BE.entity.RefreshToken;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.dto.AuthResponse;
import com.linkrap.BE.dto.JoinForm;
import com.linkrap.BE.dto.LoginRequest;
import com.linkrap.BE.repository.UsersRepository;
import com.linkrap.BE.repository.RefreshTokenRepository;
import com.linkrap.BE.repository.bulk.FriendBulkDao;
import com.linkrap.BE.repository.bulk.ScrapBulkDao;
import com.linkrap.BE.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Locale;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FriendBulkDao friendBulkDao;
    private final ScrapBulkDao scrapBulkDao;
    private final RefreshTokenRepository refreshTokenRepository;



    @Transactional
    public AuthResponse join(JoinForm form) {

        final String loginId  = form.getLoginId().trim();
        final String email    = form.getEmail().trim().toLowerCase(Locale.ROOT);
        final String nickname = form.getNickname().trim();


        if (userRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (!form.passwordsMatch()) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        Users u = new Users();
        u.setLoginId(loginId);
        u.setEmail(email);
        u.setNickname(nickname);
        u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
        u.setProfileImage(form.getProfileImageUrl());


        Users saved  = userRepository.save(u);

        String accessToken = jwtTokenProvider.generateAccessToken(
                saved.getUserId(),
                saved.getLoginId()
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(saved.getUserId());
        java.time.OffsetDateTime expiry = java.time.OffsetDateTime.now().plusDays(14);
        refreshTokenRepository.save(new RefreshToken(saved, refreshToken, expiry));



        return new AuthResponse(
                saved.getUserId(),
                saved.getLoginId(),
                saved.getEmail(),
                saved.getNickname(),
                saved.getProfileImage(),
                accessToken
        );
    }

    public record LoginTokens(String accessToken, String refreshToken) {}
    public record LoginResult(AuthResponse body, LoginTokens tokens) {}

    @Transactional
    public  LoginResult login(LoginRequest req) {
        final String loginId = req.loginId().trim();
        final String rawPw   = req.password();


        Users user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(rawPw, user.getPasswordHash())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");

        }


        String accessToken = jwtTokenProvider.generateAccessToken(user.getUserId(), user.getLoginId());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUserId());
        OffsetDateTime expiry = OffsetDateTime.now().plusDays(14);
        refreshTokenRepository.save(new RefreshToken(user, refreshToken, expiry));


        AuthResponse body = new AuthResponse(
                user.getUserId(),
                user.getLoginId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage(),
                accessToken
        );

        return new LoginResult(body, new LoginTokens(accessToken, refreshToken));
    }

    @Transactional
    public void revoke(String refreshToken, String authorizationHeader) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenRepository.deleteByToken(refreshToken);
        }

    }


    @Transactional
    public void deleteUser(Integer userId, boolean hard, String rawPassword) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()
                || rawPassword == null || rawPassword.isBlank()
                || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        refreshTokenRepository.deleteAllByUserId(userId);


        if (hard) {

            friendBulkDao.deleteAllByUserIdOrFriendId(userId);
            scrapBulkDao.deleteAllByUserId(userId);
            userRepository.delete(user);
        } else {

            user.setDeletedAt(OffsetDateTime.now());
            user.setNickname("(deleted)");
            user.setEmail(null);
            user.setProfileImage(null);

            scrapBulkDao.makePrivateByUserId(userId);

            try {
                friendBulkDao.softDetachAllForUser(userId);
            } catch (Exception ignore) {}
        }
    }

    private String extractAccessToken(String authHeader) {
        if (authHeader == null) return null;
        if (authHeader.startsWith("Bearer ")) return authHeader.substring(7);
        return null;
    }
}