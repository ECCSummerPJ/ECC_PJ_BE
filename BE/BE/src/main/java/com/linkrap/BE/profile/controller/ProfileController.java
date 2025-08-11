package com.linkrap.BE.profile.controller;

import com.linkrap.BE.profile.dto.ProfileResponseDto;
import com.linkrap.BE.profile.dto.ProfileUpdateRequestDto;
import com.linkrap.BE.profile.dto.ProfileUpdateResponseDto;
import com.linkrap.BE.profile.dto.ResponseFormat;
import com.linkrap.BE.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile")
@Tag(name = "프로필 API", description = "프로필 관련 API")
public class ProfileController {

    private final ProfileService profileService;

    // 프로필 조회
    @Operation(summary = "프로필 조회", description = "닉네임, 이메일, 프로필 이미지 반환")
    @GetMapping
    public ResponseFormat<ProfileResponseDto> getProfile(@RequestParam Long userId) { // 임시
        try {
            ProfileResponseDto dto = profileService.getProfile(userId);
            return ResponseFormat.ok("프로필 조회 성공", dto);
        } catch (IllegalArgumentException e) {
            return ResponseFormat.failure(e.getMessage());
        } catch (Exception e) {
            return ResponseFormat.error("서버 내부 오류");
        }
    }

    // 텍스트 변경 (닉네임/이메일/비밀번호)
    @Operation(summary = "닉네임/이메일/비밀번호 변경")
    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseFormat<ProfileUpdateResponseDto> updateProfile(
            @RequestParam Long userId, // 임시
            @RequestBody ProfileUpdateRequestDto req) {
        try {
            ProfileUpdateResponseDto res = profileService.updateProfile(userId, req);
            return ResponseFormat.ok("프로필 갱신 완료", res);
        } catch (IllegalArgumentException e) {
            return ResponseFormat.failure(e.getMessage());
        } catch (Exception e) {
            return ResponseFormat.error("서버 내부 오류");
        }
    }

}
