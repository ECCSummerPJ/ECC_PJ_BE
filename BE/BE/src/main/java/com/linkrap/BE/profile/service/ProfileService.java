package com.linkrap.BE.profile.service;

import com.linkrap.BE.profile.dto.ProfileResponseDto;
import com.linkrap.BE.profile.dto.ProfileUpdateRequestDto;
import com.linkrap.BE.profile.entity.User;
import com.linkrap.BE.profile.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.linkrap.BE.profile.dto.ProfileUpdateResponseDto;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final UserRepository userRepository;

    // 프로필 조회
    @Transactional(readOnly = true)
    public ProfileResponseDto getProfile(Long userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + userId));

        String imageBase64 = null;
        if (u.getProfileImage() != null && u.getProfileImage().length > 0) {
            imageBase64 = java.util.Base64.getEncoder().encodeToString(u.getProfileImage());
        }

        return new ProfileResponseDto(u.getEmail(), u.getNickname(), imageBase64);
    }


    // 닉네임/이메일/비밀번호 변경
    public ProfileUpdateResponseDto updateProfile(Long userId, ProfileUpdateRequestDto req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + userId));

        java.util.List<String> updated = new java.util.ArrayList<>();

        // 닉네임 변경
        if (req.getNickname() != null && !req.getNickname().isBlank()
                && !req.getNickname().equals(user.getNickname())) {

            // 닉네임 작성 제한 체크
            String nickname = req.getNickname().trim();
            validateNickname(nickname);

            // 중복 체크
            if (userRepository.existsByNickname(nickname)) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }
            
            user.setNickname(req.getNickname());
            updated.add("nickname");
        }

        // 이메일 변경
        if (req.getEmail() != null && !req.getEmail().isBlank()
                && !req.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(req.getEmail())) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
            user.setEmail(req.getEmail());
            updated.add("email");
        }

        // 비밀번호
        boolean wantsPwChange = (req.getCurrentPassword() != null && !req.getCurrentPassword().isBlank())
                || (req.getNewPassword() != null && !req.getNewPassword().isBlank());
        if (wantsPwChange) {
            if (req.getCurrentPassword() == null || req.getNewPassword() == null) {
                throw new IllegalArgumentException("비밀번호 변경 시 현재/새 비밀번호를 모두 입력하세요.");
            }
            if (!user.getPassword().equals(req.getCurrentPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            // 비밀번호 작성 제한 체크
            String newPw = req.getNewPassword();
            validatePassword(newPw);

            user.setPassword(newPw); // 학습용: 실제 서비스에서는 반드시 해시!
            updated.add("password");
        }

        userRepository.save(user);
        return new ProfileUpdateResponseDto(updated, user.getEmail(), user.getNickname());
    }


    private void validateNickname(String nickname) {
        // 1. 공백 불가, 길이 2자 이상 15자 이하
        if (nickname.length() < 2 || nickname.length() > 15) {
            throw new IllegalArgumentException("닉네임은 2~15자여야 합니다.");
        }
        // 2. 한글, 영문, 숫자, ._만 허용
        if (!nickname.matches("^[가-힣a-zA-Z0-9._]+$")) {
            throw new IllegalArgumentException("닉네임은 한글/영문/숫자와 특수문자 _ . 만 사용할 수 있습니다.");
        }
        // 3. 공백 체크
        if (nickname.contains(" ")) {
            throw new IllegalArgumentException("닉네임에는 공백을 사용할 수 없습니다.");
        }
    }

    private void validatePassword(String pw) {
        // 1. 길이 8자 이상 20자 이하, 공백 불가
        if (pw.length() < 8 || pw.length() > 20) {
            throw new IllegalArgumentException("비밀번호는 8~20자여야 합니다.");
        }
        if (pw.contains(" ")) {
            throw new IllegalArgumentException("비밀번호에는 공백을 사용할 수 없습니다.");
        }

        // 2. 영문/숫자/특수문자 중 2가지 이상 조합
        boolean hasLetter = pw.matches(".*[A-Za-z].*");
        boolean hasDigit  = pw.matches(".*\\d.*");
        boolean hasSpecial = pw.matches(".*[^A-Za-z0-9].*");

        int kinds = 0;
        if (hasLetter) kinds++;
        if (hasDigit) kinds++;
        if (hasSpecial) kinds++;

        if (kinds < 2) {
            throw new IllegalArgumentException("비밀번호는 영문/숫자/특수문자 중 2가지 이상을 포함해야 합니다.");
        }
    }

}
