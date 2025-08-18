
package com.linkrap.BE.service;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.entity.Users;
import com.linkrap.BE.repository.ScrapViewRepository;
import com.linkrap.BE.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class ProfileService {

    @Autowired UsersRepository usersRepository;
    @Autowired ScrapViewRepository scrapViewRepository;
    @Autowired S3Client s3Client;


    @Value("${APP_STORAGE:local}")         private String storage;     // s3 또는 local
    @Value("${APP_S3_BUCKET:}")            private String bucket;
    @Value("${APP_S3_REGION:us-east-1}")   private String s3Region;
    @Value("${APP_UPLOAD_DIR:./uploads}")
    private String uploadDir;
    // 프로필 조회
    @Transactional(readOnly = true)
    public ProfileDto getProfile(int userId) {
        Users u = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + userId));

        return new ProfileDto(u.getEmail(), u.getNickname(), u.getProfileImage());
    }


    // 닉네임/이메일/비밀번호 변경
    public ProfileUpdateResponseDto updateProfile(int userId, ProfileUpdateRequestDto dto) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + userId));

        Map<String, Boolean> hasUpdated = new HashMap<>();
        hasUpdated.put("nickname", false);
        hasUpdated.put("email", false);
        hasUpdated.put("password", false);

        // 닉네임 변경
        if (dto.getNickname() != null && !dto.getNickname().isBlank()
                && !dto.getNickname().equals(user.getNickname())) {

            // 닉네임 작성 제한 체크
            String nickname = dto.getNickname().trim(); // 공백 방지
            validateNickname(nickname);

            // 중복 체크
            if (usersRepository.existsByNickname(nickname)) {
                throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
            }

            user.setNickname(nickname);
            hasUpdated.put("nickname", true);
        }

        // 이메일 변경
        if (dto.getEmail() != null && !dto.getEmail().isBlank()
                && !dto.getEmail().equals(user.getEmail())) {
            if (usersRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
            String email = dto.getEmail().trim();
            user.setEmail(email);
            hasUpdated.put("email", true);
        }

        // 비밀번호
        boolean wantsPwChange = (dto.getCurrentPassword() != null && !dto.getCurrentPassword().isBlank())
                || (dto.getNewPassword() != null && !dto.getNewPassword().isBlank());
        if (wantsPwChange) {
            if (dto.getCurrentPassword() == null || dto.getNewPassword() == null) {
                throw new IllegalArgumentException("비밀번호 변경 시 현재/새 비밀번호를 모두 입력하세요.");
            }
            if (!user.getPasswordHash().equals(dto.getCurrentPassword())) {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }

            // 비밀번호 작성 제한 체크
            String newPw = dto.getNewPassword();
            validatePassword(newPw);

            user.setPasswordHash(newPw); // 학습용: 실제 서비스에서는 반드시 해시!
            hasUpdated.put("password", true);
        }

        usersRepository.save(user);
        ProfileDto profileDto = new ProfileDto(user.getEmail(), user.getNickname(), user.getProfileImage());

        // >>> 새로운 DTO 형태로 반환 (hasUpdated + profile)
        return new ProfileUpdateResponseDto(hasUpdated, profileDto);
    }

    // 프로필 이미지 변경
    public ProfileImageUpdateResponseDto updateProfileImage(int userId, MultipartFile file) {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("이미지 파일이 비어 있습니다.");

        log.info("ProfileImage storage={}, bucket={}, region={}", storage, bucket, s3Region);
        log.info("ENV APP_STORAGE={}, APP_S3_BUCKET={}, APP_S3_REGION={}",
                System.getenv("APP_STORAGE"), System.getenv("APP_S3_BUCKET"), System.getenv("APP_S3_REGION"));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. id=" + userId));

        try {
            String original = file.getOriginalFilename();
            String ext = (original != null && original.contains(".")) ? original.substring(original.lastIndexOf(".")) : "";
            String saved = java.util.UUID.randomUUID() + ext;

            String url;
            if ("s3".equalsIgnoreCase(storage)) {
                // --- S3 업로드 (ACL 제거) ---
                String key = "profile-images/" + saved;

                PutObjectRequest put = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(file.getContentType())
                        .cacheControl("public, max-age=31536000")
                        .build();

                try {
                    s3Client.putObject(put, RequestBody.fromBytes(file.getBytes()));
                } catch (S3Exception e) {
                    // 에러코드/메시지를 상세히 기록
                    log.error("S3 putObject failed: code={}, message={}",
                            e.awsErrorDetails().errorCode(), e.awsErrorDetails().errorMessage(), e);
                    throw e;
                }

                // 리전 포함 URL (권장)
                url = "https://" + bucket + ".s3." + s3Region + ".amazonaws.com/" + key;

            } else {
                // --- 기존 로컬 저장 ---
                Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
                Path dir  = root.resolve("profile-images");
                Files.createDirectories(dir);

                Path dest = dir.resolve(saved).toAbsolutePath().normalize();
                file.transferTo(dest.toFile());

                url = "/uploads/profile-images/" + saved; // WebMvcConfig 서빙
            }

            user.setProfileImage(url);
            usersRepository.save(user);
            return new ProfileImageUpdateResponseDto(true, url);

        } catch (Exception e) {
            throw new IllegalStateException("프로필 이미지 저장에 실패했습니다.", e);
        }
    }

    // 통계
    @Transactional(readOnly = true)
    public ProfileStatisticsDto getStatistics(int userId) {
        Pageable top5 = PageRequest.of(0, 5);

        var topScraps = scrapViewRepository.findTopViewedScrapsByOwner(userId, top5);

        var topCategories = scrapViewRepository.findTopCategoriesByOwner(userId, top5);

        return new ProfileStatisticsDto(topScraps, topCategories);
    }


    // 닉네임 작성 제한
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

    // 비밀번호 작성 제한
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

