
package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
@Tag(name = "profile-controller")
public class ProfileApiController {

    private final ProfileService profileService;

    // 프로필 조회
    @Operation(summary = "프로필 조회", description = "닉네임, 이메일, 프로필 이미지 반환")
    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(@RequestParam int userId) { // 임시
        try {
            ProfileDto dto = profileService.getProfile(userId);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 텍스트 변경 (닉네임/이메일/비밀번호)
    @Operation(summary = "닉네임/이메일/비밀번호 변경")
    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(
            @RequestParam int userId, // 임시
            @RequestBody ProfileUpdateRequestDto req) {
        try {
            ProfileUpdateResponseDto res = profileService.updateProfile(userId, req);
            return ResponseEntity.status(HttpStatus.OK).body(res);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 프로필 이미지 변경
    @Operation(summary = "프로필 이미지 변경")
    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageUpdateResponseDto> updateProfileImage(
            @RequestParam int userId,                 // 임시: 로그인 붙기 전까지
            @RequestPart("file") MultipartFile file) {

        try {
            ProfileImageUpdateResponseDto dto = profileService.updateProfileImage(userId, file);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 통계
    @Operation(summary = "프로필 통계 (전체기간, TOP5)")
    @GetMapping("/statistics")
    public ResponseEntity<ProfileStatisticsDto> getStatistics(@RequestParam int userId) {
        try {
            ProfileStatisticsDto dto = profileService.getStatistics(userId);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

