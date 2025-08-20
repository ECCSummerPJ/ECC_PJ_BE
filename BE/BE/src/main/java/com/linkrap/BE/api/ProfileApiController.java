
package com.linkrap.BE.api;

import com.linkrap.BE.dto.*;
import com.linkrap.BE.security.CustomUserDetails;
import com.linkrap.BE.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/profile")
@Tag(name = "profile-controller")
@SecurityRequirement(name="bearerAuth")
public class ProfileApiController {

    private final ProfileService profileService;

    // 프로필 조회
    @Operation(summary = "프로필 조회", description = "닉네임, 이메일, 프로필 이미지 반환")
    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        ProfileDto dto = profileService.getProfile(user.getUserId());
        return ResponseEntity.ok(dto);
    }

    // 텍스트 변경 (닉네임/이메일/비밀번호)
    @Operation(summary = "닉네임/이메일/비밀번호 변경")
    @PatchMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProfileUpdateResponseDto> updateProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ProfileUpdateRequestDto req) {
            ProfileUpdateResponseDto res = profileService.updateProfile(user.getUserId(), req);
            return ResponseEntity.ok(res);
        }


    // 프로필 이미지 변경
    @Operation(summary = "프로필 이미지 변경")
    @PutMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageUpdateResponseDto> updateProfileImage(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("file") MultipartFile file) {

        ProfileImageUpdateResponseDto dto = profileService.updateProfileImage(user.getUserId(), file);
        return ResponseEntity.ok(dto);
    }

    // 통계
    @Operation(summary = "프로필 통계 (전체기간, TOP5)")
    @GetMapping("/statistics")
    public ResponseEntity<ProfileStatisticsDto> getStatistics(@AuthenticationPrincipal CustomUserDetails user) {
        ProfileStatisticsDto dto = profileService.getStatistics(user.getUserId());
        return ResponseEntity.ok(dto);
    }

    // (1) 서비스에서 IllegalArgumentException 던진 경우 -> 400 + {"message":"..."}
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(IllegalArgumentException e) {
        return Map.of("message", e.getMessage());
    }

    // (2) 서비스에서 ResponseStatusException 던진 경우(예: BAD_REQUEST) -> 해당 상태코드 + {"message":"..."}
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleRSE(org.springframework.web.server.ResponseStatusException e) {
        // reason 이 null일 수 있어 기본 메시지 보정
        String msg = (e.getReason() != null) ? e.getReason() : e.getMessage();
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", msg));
    }

    // (3) 파일 저장 실패 등 서버쪽 예외 -> 500 + {"message":"..."}
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleISE(IllegalStateException e) {
        return Map.of("message", e.getMessage());
    }
}

